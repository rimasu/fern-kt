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

import com.github.rimasu.node.types.Node
import com.github.rimasu.node.types.StructNode
import com.github.rimasu.text.Region

/**
 * State that creates a [StructNode]. Contains three sub-states that manage
 * parsing the field name, the equals sign and the values. A close struct
 * terminates the state and returns the parent state.
 */
internal class StructNodeState(
        private val parent: ParentState,
        private val startLine: Int,
        private val startColumn: Int
) : ParentState()
{
    private val nodes = mutableMapOf<String, Node>()
    private val inField = FieldNameState()
    private val preAssign = PreAssign()
    private val postAssign = PostAssign()

    override fun push(node: Node) {
        nodes.put(inField.popFieldName(), node)
    }

    override fun push(type: CodePointType, codePoint: Int, line: Int, column: Int): State {
        return when(type) {
            CodePointType.NORMAL -> inField.push(type, codePoint, line, column)
            CodePointType.CLOSE_STRUCT -> finishStruct(line, column)
            CodePointType.WHITE_SPACE -> this
            else -> ErrorState(line, column)
        }
    }

    private fun finishStruct(endLine: Int, endColumn: Int): State {
        parent.push(StructNode(nodes, Region(startLine, startColumn, endLine, endColumn)))
        return parent
    }

    /**
     * State during field name capture. Any normal character
     * is appended to field name. The assignment character
     * triggers a [post assignment state][PostAssign] and
     * a white space triggers a [pre assignment state][PreAssign]
     */
    private inner class FieldNameState : State() {
        private val field = StringBuilder()

        override fun push(type: CodePointType, codePoint: Int, line: Int, column: Int): State {
            return when(type) {
                CodePointType.NORMAL ->  { field.appendCodePoint(codePoint); return this }
                CodePointType.ASSIGNMENT -> postAssign
                CodePointType.WHITE_SPACE -> preAssign
                else -> ErrorState(line, column)
            }
        }

        fun popFieldName(): String {
            val name = field.toString()
            field.setLength(0)
            return name
        }
    }

    /**
     * State after a field name and before an assignment.
     * Only valid value is white space or assignment.
     */
    private inner class PreAssign : State() {
        override fun push(type: CodePointType, codePoint: Int, line: Int, column: Int): State {
            return when(type) {
                CodePointType.ASSIGNMENT -> postAssign
                CodePointType.WHITE_SPACE -> this
                else -> ErrorState(line, column)
            }
        }
    }

    /**
     * State after assignment before value. Any legal start value is supported.
     * White space is ignored.
     */
    private inner class PostAssign : State() {
        override fun push(type: CodePointType, codePoint: Int, line: Int, column: Int): State {
            return when(type) {
                CodePointType.NORMAL ->  LeafNodeState(this@StructNodeState, line, column).push(type, codePoint, line, column)
                CodePointType.QUOTE -> QuotedLeafNodeState(this@StructNodeState, line, column)
                CodePointType.OPEN_STRUCT -> StructNodeState(this@StructNodeState, line, column)
                CodePointType.OPEN_LIST -> ListNodeState(this@StructNodeState, line, column)
                CodePointType.WHITE_SPACE -> this
                else -> ErrorState(line, column)
            }
        }
    }
}