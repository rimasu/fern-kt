package com.github.rimasu.node.decoder

internal enum class CodePointType {

    NORMAL,

    OPEN_PARENTHESIS,

    CLOSE_PARENTHESIS,

    WHITE_SPACE;

    companion object {

        private val OPEN_PARENTHESIS_CODE_POINT= '('.toInt()
        private val CLOSE_PARENTHESIS_CODE_POINT= ')'.toInt()
        private val SPACE_CODE_POINT= ' '.toInt()
        private val TAB_CODE_POINT= '\t'.toInt()
        private val NEW_LINE_CODE_POINT= '\n'.toInt()
        private val CARRIAGE_RETURN_CODE_POINT= '\r'.toInt()

        fun classify(codePoint: Int): CodePointType {
            return when(codePoint) {
                OPEN_PARENTHESIS_CODE_POINT -> OPEN_PARENTHESIS
                CLOSE_PARENTHESIS_CODE_POINT -> CLOSE_PARENTHESIS
                SPACE_CODE_POINT -> WHITE_SPACE
                TAB_CODE_POINT -> WHITE_SPACE
                NEW_LINE_CODE_POINT -> WHITE_SPACE
                CARRIAGE_RETURN_CODE_POINT -> WHITE_SPACE
                else -> NORMAL
            }
        }
    }
}