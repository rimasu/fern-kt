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

import com.github.rimasu.node.types.ListNode
import com.github.rimasu.node.types.Node
import com.github.rimasu.text.Region

/**
 * List node state. Captures a list of values. Any legal start of value
 * is captured. A close list terminates the state and returns the parent
 * state.
 */
internal class ListNodeState(
        private val parent: ParentState,
        private val startLine: Int,
        private val startColumn: Int
) : ParentState()
{
    private val nodes = mutableListOf<Node>()

    override fun push(node: Node) { nodes.add(node) }

    override fun push(type: CodePointType, codePoint: Int, line: Int, column: Int): State {
        return when(type) {
            CodePointType.NORMAL -> LeafNodeState(this, line, column).push(type, codePoint, line, column)
            CodePointType.QUOTE -> QuotedLeafNodeState(this, line, column)
            CodePointType.OPEN_STRUCT -> StructNodeState(this, line, column)
            CodePointType.OPEN_LIST -> ListNodeState(this, line, column)
            CodePointType.CLOSE_LIST -> finishList(line, column)
            CodePointType.WHITE_SPACE -> this
            else -> ErrorState(line, column)
        }
    }

    private fun finishList(endLine: Int, endColumn: Int): State {
        parent.push(ListNode(nodes, Region(startLine, startColumn, endLine, endColumn)))
        return parent
    }
}