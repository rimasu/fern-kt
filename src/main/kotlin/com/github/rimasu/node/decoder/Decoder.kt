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

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.rimasu.node.types.*
import com.github.rimasu.text.Position

object Decoder {

    fun parse(s: String) : Result<Node, DecoderError> {
        val root = RootState()
        var state: State = root
        var line = 1
        var column = 0
        s.codePoints().forEach {
            if (it == CodePointType.LINE_FEED) {
                line++
                column = 0
            } else {
                column++
            }
            val type = CodePointType.classify(it)
            state = state.push(type, it, line, column)
        }

        return decode(state, line, column)
    }

    private fun decode(state: State, line: Int, column: Int): Result<Node, DecoderError> {
        return when(state) {
            is RootState -> Ok(state.value)
            is ErrorState -> Err(DecoderError(state.position))
            else -> Err(DecoderError(Position(line, column)))
        }
    }
}


