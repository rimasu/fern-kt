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

import com.winterbe.expekt.should
import org.junit.jupiter.api.Test

class CodePointTypeTest {

    @Test
    fun openParenthesisClassifiedAsOpenStruct() {
        CodePointType.classify(CodePointType.OPEN_PARENTHESIS_CODE_POINT).should.equal(CodePointType.OPEN_STRUCT)
    }

    @Test
    fun closeParenthesisClassifiedAsCloseStruct() {
        CodePointType.classify(CodePointType.CLOSE_PARENTHESIS_CODE_POINT).should.equal(CodePointType.CLOSE_STRUCT)
    }

    @Test
    fun openSquareBracketsClassifiedAsOpenList() {
        CodePointType.classify(CodePointType.OPEN_SQUARE_BRACKETS_CODE_POINT).should.equal(CodePointType.OPEN_LIST)
    }

    @Test
    fun closeSquareBracketsClassifiedAsCloseList() {
        CodePointType.classify(CodePointType.CLOSE_SQUARE_BRACKETS_CODE_POINT).should.equal(CodePointType.CLOSE_LIST)
    }

    @Test
    fun equalsClassifiedAsAssignment() {
        CodePointType.classify(CodePointType.EQUALS_CODE_POINT).should.equal(CodePointType.ASSIGNMENT)
    }

    @Test
    fun doubleQuoteClassifiedAsQuote() {
        CodePointType.classify(CodePointType.DOUBLE_QUOTE_CODE_POINT).should.equal(CodePointType.QUOTE)
    }

    @Test
    fun pipeClassifiedAsEscape() {
        CodePointType.classify(CodePointType.PIPE_CODE_POINT).should.equal(CodePointType.ESCAPE)
    }

    @Test
    fun spaceClassifiedAsWhiteSpace() {
        CodePointType.classify(' '.toInt()).should.equal(CodePointType.WHITE_SPACE)
    }

    @Test
    fun tabClassifiedAsWhiteSpace() {
        CodePointType.classify('\t'.toInt()).should.equal(CodePointType.WHITE_SPACE)
    }

    @Test
    fun lineFeedClassifiedAsWhiteSpace() {
        CodePointType.classify(CodePointType.LINE_FEED).should.equal(CodePointType.WHITE_SPACE)
    }

    @Test
    fun verticalTabClassifiedAsWhiteSpace() {
        CodePointType.classify(0x0B).should.equal(CodePointType.WHITE_SPACE)
    }

    @Test
    fun formFeedTabClassifiedAsWhiteSpace() {
        CodePointType.classify(0x0C).should.equal(CodePointType.WHITE_SPACE)
    }

    @Test
    fun carriageReturnClassifiedAsWhiteSpace() {
        CodePointType.classify('\r'.toInt()).should.equal(CodePointType.WHITE_SPACE)
    }

    @Test
    fun fileSeparatorTabClassifiedAsWhiteSpace() {
        CodePointType.classify(0x1C).should.equal(CodePointType.WHITE_SPACE)
    }

    @Test
    fun groupSeparatorTabClassifiedAsWhiteSpace() {
        CodePointType.classify(0x1D).should.equal(CodePointType.WHITE_SPACE)
    }

    @Test
    fun recordSeparatorTabClassifiedAsWhiteSpace() {
        CodePointType.classify(0x1E).should.equal(CodePointType.WHITE_SPACE)
    }

    @Test
    fun unitSeparatorTabClassifiedAsWhiteSpace() {
        CodePointType.classify(0x1F).should.equal(CodePointType.WHITE_SPACE)
    }





    @Test
    fun aClassifiedAsNormal() {
        CodePointType.classify('a'.toInt()).should.equal(CodePointType.NORMAL)
    }

    @Test
    fun maxUnicodeChareClassifiedAsNormal() {
        CodePointType.classify(0x10FFFF).should.equal(CodePointType.NORMAL)
    }
}