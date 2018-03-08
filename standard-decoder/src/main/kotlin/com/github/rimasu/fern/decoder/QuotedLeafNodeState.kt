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
package com.github.rimasu.fern.decoder

import com.github.rimasu.fern.types.LeafNode
import com.github.rimasu.text.Region

/**
 * Parses the content of quoted value. All code points are captured apart
 * from quote and escape characters. Any character after escape is captured.
 * An unescaped quote terminates the value and returns the parent state.
 */
internal class QuotedLeafNodeState(
        private val parent: ParentState,
        private val startLine: Int,
        private val startColumn: Int
) : State()
{
    private val value = StringBuilder()
    private val escaped = Escaped()

    override fun push(type: CodePointType, codePoint: Int, line: Int, column: Int) : State {
        return when(type) {
            CodePointType.QUOTE -> {
                parent.push(LeafNode(value.toString(), Region(startLine, startColumn, line, column)))
                parent
            }
            CodePointType.ESCAPE -> escaped
            else -> { value.appendCodePoint(codePoint); this}
        }
    }

    private inner class Escaped : State() {
        override fun push(type: CodePointType, codePoint: Int, line: Int, column: Int): State {
            value.appendCodePoint(codePoint)
            return this@QuotedLeafNodeState

        }
    }
}