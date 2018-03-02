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
import com.github.rimasu.node.decoder.CodePointType.Companion.CLOSE_PARENTHESIS_CODE_POINT
import com.github.rimasu.node.decoder.CodePointType.Companion.CLOSE_SQUARE_BRACKETS_CODE_POINT
import com.github.rimasu.node.decoder.CodePointType.Companion.DOUBLE_QUOTE_CODE_POINT
import com.github.rimasu.node.decoder.CodePointType.Companion.EQUALS_CODE_POINT
import com.github.rimasu.node.decoder.CodePointType.Companion.LINE_FEED
import com.github.rimasu.node.decoder.CodePointType.Companion.OPEN_PARENTHESIS_CODE_POINT
import com.github.rimasu.node.decoder.CodePointType.Companion.OPEN_SQUARE_BRACKETS_CODE_POINT
import com.github.rimasu.node.decoder.CodePointType.Companion.PIPE_CODE_POINT
import com.github.rimasu.node.decoder.CodePointType.Companion.classify
import org.junit.Test
import kotlin.test.assertEquals

class CodePointTypeTest {

    @Test
    fun openParenthesisClassifiedAsOpenStruct() {
        assertEquals(OPEN_STRUCT, classify(OPEN_PARENTHESIS_CODE_POINT))
    }

    @Test
    fun closeParenthesisClassifiedAsCloseStruct() {
        assertEquals(CLOSE_STRUCT, classify(CLOSE_PARENTHESIS_CODE_POINT))
    }

    @Test
    fun openSquareBracketsClassifiedAsOpenList() {
        assertEquals(OPEN_LIST, classify(OPEN_SQUARE_BRACKETS_CODE_POINT))
    }

    @Test
    fun closeSquareBracketsClassifiedAsCloseList() {
        assertEquals(CLOSE_LIST, classify(CLOSE_SQUARE_BRACKETS_CODE_POINT))
    }

    @Test
    fun equalsClassifiedAsAssignment() {
        assertEquals(ASSIGNMENT, classify(EQUALS_CODE_POINT))
    }

    @Test
    fun doubleQuoteClassifiedAsQuote() {
        assertEquals(QUOTE, classify(DOUBLE_QUOTE_CODE_POINT))
    }

    @Test
    fun pipeClassifiedAsEscape() {
        assertEquals(ESCAPE, classify(PIPE_CODE_POINT))
    }

    @Test
    fun spaceClassifiedAsWhiteSpace() {
        assertEquals(WHITE_SPACE, classify(' '.toInt()))
    }

    @Test
    fun tabClassifiedAsWhiteSpace() {
        assertEquals(WHITE_SPACE, classify('\t'.toInt()))
    }

    @Test
    fun lineFeedClassifiedAsWhiteSpace() {
        assertEquals(WHITE_SPACE, classify(LINE_FEED))
    }

    @Test
    fun verticalTabClassifiedAsWhiteSpace() {
        assertEquals(WHITE_SPACE, classify(0x0B))
    }

    @Test
    fun formFeedTabClassifiedAsWhiteSpace() {
        assertEquals(WHITE_SPACE, classify(0x0C))
    }

    @Test
    fun carriageReturnClassifiedAsWhiteSpace() {
        assertEquals(WHITE_SPACE, classify('\r'.toInt()))
    }

    @Test
    fun fileSeparatorTabClassifiedAsWhiteSpace() {
        assertEquals(WHITE_SPACE, classify(0x1C))
    }

    @Test
    fun groupSeparatorTabClassifiedAsWhiteSpace() {
        assertEquals(WHITE_SPACE, classify(0x1D))
    }

    @Test
    fun recordSeparatorTabClassifiedAsWhiteSpace() {
        assertEquals(WHITE_SPACE, classify(0x1E))
    }

    @Test
    fun unitSeparatorTabClassifiedAsWhiteSpace() {
        assertEquals(WHITE_SPACE, classify(0x1F))
    }

    @Test
    fun aClassifiedAsNormal() {
        assertEquals(NORMAL, classify('a'.toInt()))
    }

    @Test
    fun maxUnicodeChareClassifiedAsNormal() {
        assertEquals(NORMAL, classify(0x10FFFF))
    }
}