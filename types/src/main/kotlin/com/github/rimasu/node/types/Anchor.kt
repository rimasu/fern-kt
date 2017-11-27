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
package com.github.rimasu.node.types

/**
 * Location within a document.
 */
data class Anchor(
        /** Line the block starts on, first line is line one. */
        val startLine: Int,

        /** Position in the line the block starts on, first character is character one. */
        val startPos: Int,

        /** Line the block ends on. Must be greater than or equal to start line. */
        val endLine: Int,

        /** Position in the line the block ends, first character is one. Must be greater
         * than or equal to start pos is start and end line are the same.
         */
        val endPos: Int
) {
    init {
        require(startLine > 0) {"start line must be greater than zero"}
        require(endLine >= startLine) {"end line must be greater than or equal to start line"}
        require(startPos > 0) {"start pos must be greater than zero"}
        if (startLine == endLine) {
            require(endPos >= startPos) {"end pos must be greater than or equal to start pos (if start line = end line)"}
        } else {
            require(endPos > 0) {"end pos must be greater than zero (if start line > end line)"}
        }
    }

    override fun toString(): String {
        return if(startLine == endLine) {
            if (startPos == endPos) {
                "$startLine,$startPos"
            } else {
                "$startLine,$startPos->$endPos"
            }
        } else {
            "$startLine,$startPos->$endLine,$endPos"
        }
    }
}