package com.github.rimasu.node.decoder

import com.winterbe.expekt.should
import org.junit.jupiter.api.Test

class CodePointTypeTest {

    @Test
    fun openParenthesisClassifiedAsOpenParenthesis() {
        CodePointType.classify('('.toInt()).should.equal(CodePointType.OPEN_PARENTHESIS)
    }

    @Test
    fun closeParenthesisClassifiedAsCloseParenthesis() {
        CodePointType.classify(')'.toInt()).should.equal(CodePointType.CLOSE_PARENTHESIS)
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
    fun newLineClassifiedAsWhiteSpace() {
        CodePointType.classify('\n'.toInt()).should.equal(CodePointType.WHITE_SPACE)
    }

    @Test
    fun carriageReturnClassifiedAsWhiteSpace() {
        CodePointType.classify('\r'.toInt()).should.equal(CodePointType.WHITE_SPACE)
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