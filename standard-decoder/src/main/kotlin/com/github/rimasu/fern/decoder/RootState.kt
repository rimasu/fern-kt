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

import com.github.rimasu.fern.decoder.CodePointType.*
import com.github.rimasu.fern.types.Node
import com.github.rimasu.fern.types.NullNode
import com.github.rimasu.text.Position

/**
 * State that parses root of document.
 * Only acceptable code points are open struct, open list and white space.
 */
internal class RootState : ParentState() {

    var value: Node = NullNode()

    override fun push(type: CodePointType, codePoint: Int, line: Int, column: Int): State {
        return when(type) {
            OPEN_STRUCT -> StructNodeState(this, line, column)
            OPEN_LIST -> ListNodeState(this, line, column)
            WHITE_SPACE -> this
            else -> ErrorState(
                    expectedTypes = listOf(OPEN_STRUCT, OPEN_LIST, WHITE_SPACE),
                    position = Position(line, column)
            )
        }
    }

    override fun push(node: Node) {
        this.value = node
    }
}