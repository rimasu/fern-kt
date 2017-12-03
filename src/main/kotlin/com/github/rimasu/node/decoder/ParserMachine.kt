/**
 * Copyright 2017 Richard Sunderland
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package com.github.rimasu.node.decoder

import com.github.rimasu.node.decoder.CodePointType.*
import com.github.rimasu.node.types.*

internal abstract class State {
    abstract fun push(type: CodePointType, codePoint: Int) : State
}

internal abstract class ParentState : State() {
    abstract fun push(node: Node)
}


internal class ErrorState : State() {
    override fun push(type: CodePointType, codePoint: Int): State {
        return this
    }
}

internal class RootState : ParentState() {

    var value: Node = NullNode()

    override fun push(type: CodePointType, codePoint: Int): State {
        return when(type) {
            OPEN_STRUCT -> StructNodeState(this)
            OPEN_LIST -> ListNodeState(this)
            WHITE_SPACE -> this
            else -> ErrorState()
        }
    }

    override fun push(node: Node) {
        this.value = node
    }
}


internal class LeafNodeState(private val parent: ParentState) : State()
{
    private val value = StringBuilder()

    override fun push(type: CodePointType, codePoint: Int) : State {
        return when(type) {
            NORMAL -> {
                value.appendCodePoint(codePoint)
                this
            }
            else -> {
                parent.push(value.toString().asNode())
                parent.push(type, codePoint)
            }
        }
    }
}

internal class ListNodeState(private val parent: ParentState) : ParentState()
{
    private val nodes = mutableListOf<Node>()

    override fun push(node: Node) { nodes.add(node) }

    override fun push(type: CodePointType, codePoint: Int): State {
        return when(type) {
            NORMAL -> LeafNodeState(this).push(type, codePoint)
            OPEN_STRUCT -> StructNodeState(this)
            OPEN_LIST -> ListNodeState(this)
            CLOSE_LIST -> finishList()
            WHITE_SPACE -> this
            else -> ErrorState()
        }
    }

    private fun finishList(): State
    {
        parent.push(ListNode(nodes))
        return parent
    }
}

internal class StructNodeState(private val parent: ParentState) : ParentState()
{
    private val nodes = mutableMapOf<String, Node>()
    private val inField = FieldNameState()
    private val preAssign = PreAssign()
    private val postAssign = PostAssign()

    override fun push(node: Node) {
        nodes.put(inField.popFieldName(), node)
    }

    override fun push(type: CodePointType, codePoint: Int): State {
        return when(type) {
            NORMAL -> inField.push(type, codePoint)
            CLOSE_STRUCT -> finishStruct()
            WHITE_SPACE -> this
            else -> ErrorState()
        }
    }

    private fun finishStruct(): State
    {
        parent.push(StructNode(nodes))
        return parent
    }

    private inner class FieldNameState : State() {
        private val field = StringBuilder()

        override fun push(type: CodePointType, codePoint: Int): State {
            return when(type) {
                NORMAL ->  { field.appendCodePoint(codePoint); return this }
                ASSIGNMENT -> postAssign
                WHITE_SPACE -> preAssign
                else -> ErrorState()
            }
        }

        fun popFieldName(): String {
            val name = field.toString()
            field.setLength(0)
            return name
        }
    }

    private inner class PreAssign : State() {
        override fun push(type: CodePointType, codePoint: Int): State {
            return when(type) {
                ASSIGNMENT -> postAssign
                else -> ErrorState()
            }
        }
    }

    private inner class PostAssign : State() {
        override fun push(type: CodePointType, codePoint: Int): State {
            return when(type) {
                NORMAL ->  LeafNodeState(this@StructNodeState).push(type, codePoint)
                OPEN_STRUCT -> StructNodeState(this@StructNodeState)
                OPEN_LIST -> ListNodeState(this@StructNodeState)
                WHITE_SPACE -> this
                else -> ErrorState()
            }
        }
    }
}
