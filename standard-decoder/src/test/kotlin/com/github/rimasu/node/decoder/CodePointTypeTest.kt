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

import org.junit.Test
import kotlin.test.assertEquals

class CodePointTypeTest {

    @Test
    fun openParenthesisClassifiedAsOpenStruct() {
        assertEquals(CodePointType.OPEN_STRUCT, CodePointType.classify(CodePointType.OPEN_PARENTHESIS_CODE_POINT))
    }

    @Test
    fun closeParenthesisClassifiedAsCloseStruct() {
        assertEquals(CodePointType.CLOSE_STRUCT, CodePointType.classify(CodePointType.CLOSE_PARENTHESIS_CODE_POINT))
    }

    @Test
    fun openSquareBracketsClassifiedAsOpenList() {
        assertEquals(CodePointType.OPEN_LIST, CodePointType.classify(CodePointType.OPEN_SQUARE_BRACKETS_CODE_POINT))
    }

    @Test
    fun closeSquareBracketsClassifiedAsCloseList() {
        assertEquals(CodePointType.CLOSE_LIST, CodePointType.classify(CodePointType.CLOSE_SQUARE_BRACKETS_CODE_POINT))
    }

    @Test
    fun equalsClassifiedAsAssignment() {
        assertEquals(CodePointType.ASSIGNMENT, CodePointType.classify(CodePointType.EQUALS_CODE_POINT))
    }

    @Test
    fun doubleQuoteClassifiedAsQuote() {
        assertEquals(CodePointType.QUOTE, CodePointType.classify(CodePointType.DOUBLE_QUOTE_CODE_POINT))
    }

    @Test
    fun pipeClassifiedAsEscape() {
        assertEquals(CodePointType.ESCAPE, CodePointType.classify(CodePointType.PIPE_CODE_POINT))
    }

    @Test
    fun spaceClassifiedAsWhiteSpace() {
        assertEquals(CodePointType.WHITE_SPACE, CodePointType.classify(' '.toInt()))
    }

    @Test
    fun tabClassifiedAsWhiteSpace() {
        assertEquals(CodePointType.WHITE_SPACE, CodePointType.classify('\t'.toInt()))
    }

    @Test
    fun lineFeedClassifiedAsWhiteSpace() {
        assertEquals(CodePointType.WHITE_SPACE, CodePointType.classify(CodePointType.LINE_FEED))
    }

    @Test
    fun verticalTabClassifiedAsWhiteSpace() {
        assertEquals(CodePointType.WHITE_SPACE, CodePointType.classify(0x0B))
    }

    @Test
    fun formFeedTabClassifiedAsWhiteSpace() {
        assertEquals(CodePointType.WHITE_SPACE, CodePointType.classify(0x0C))
    }

    @Test
    fun carriageReturnClassifiedAsWhiteSpace() {
        assertEquals(CodePointType.WHITE_SPACE, CodePointType.classify('\r'.toInt()))
    }

    @Test
    fun fileSeparatorTabClassifiedAsWhiteSpace() {
        assertEquals(CodePointType.WHITE_SPACE, CodePointType.classify(0x1C))
    }

    @Test
    fun groupSeparatorTabClassifiedAsWhiteSpace() {
        assertEquals(CodePointType.WHITE_SPACE, CodePointType.classify(0x1D))
    }

    @Test
    fun recordSeparatorTabClassifiedAsWhiteSpace() {
        assertEquals(CodePointType.WHITE_SPACE, CodePointType.classify(0x1E))
    }

    @Test
    fun unitSeparatorTabClassifiedAsWhiteSpace() {
        assertEquals(CodePointType.WHITE_SPACE, CodePointType.classify(0x1F))
    }

    @Test
    fun aClassifiedAsNormal() {
        assertEquals(CodePointType.NORMAL, CodePointType.classify('a'.toInt()))
    }

    @Test
    fun maxUnicodeChareClassifiedAsNormal() {
        assertEquals(CodePointType.NORMAL, CodePointType.classify(0x10FFFF))
    }
}