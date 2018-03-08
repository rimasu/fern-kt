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

/**
 * Enumeration of code points that have special meaning in the format.
 */
internal enum class CodePointType {

    NORMAL,

    OPEN_STRUCT,

    CLOSE_STRUCT,

    OPEN_LIST,

    CLOSE_LIST,

    QUOTE,

    ESCAPE,

    ASSIGNMENT,

    WHITE_SPACE;

    internal companion object {

        internal val OPEN_PARENTHESIS_CODE_POINT= '('.toInt()
        internal val CLOSE_PARENTHESIS_CODE_POINT= ')'.toInt()
        internal val OPEN_SQUARE_BRACKETS_CODE_POINT= '['.toInt()
        internal val CLOSE_SQUARE_BRACKETS_CODE_POINT= ']'.toInt()
        internal val EQUALS_CODE_POINT = '='.toInt()
        internal val PIPE_CODE_POINT = '|'.toInt()
        internal val DOUBLE_QUOTE_CODE_POINT = '"'.toInt()
        internal val LINE_FEED = '\n'.toInt()

        fun classify(codePoint: Int): CodePointType {
            return if (Character.isWhitespace(codePoint)) {
                WHITE_SPACE
            } else {
                when (codePoint) {
                    OPEN_PARENTHESIS_CODE_POINT -> OPEN_STRUCT
                    CLOSE_PARENTHESIS_CODE_POINT -> CLOSE_STRUCT
                    OPEN_SQUARE_BRACKETS_CODE_POINT -> OPEN_LIST
                    CLOSE_SQUARE_BRACKETS_CODE_POINT -> CLOSE_LIST
                    DOUBLE_QUOTE_CODE_POINT -> QUOTE
                    EQUALS_CODE_POINT -> ASSIGNMENT
                    PIPE_CODE_POINT -> ESCAPE
                    else -> NORMAL
                }
            }
        }
    }
}