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

import com.winterbe.expekt.should
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class LeafNodeTest : NodeTest() {

    companion object {
        private val INTEGER_VALUE = 345234
        private val STRING_WITH_INTEGER_CONTENT = INTEGER_VALUE.toString()
        private val STRING_WITH_NON_INTEGER_CONTENT = "Some non-integer text"
    }

    val INT_NODE = STRING_WITH_INTEGER_CONTENT.asNode()
    val STRING_NODE = STRING_WITH_NON_INTEGER_CONTENT.asNode()

    override val node = INT_NODE

    @Test
    fun `leaf node with same string content are equal`() {
        val first = INTEGER_VALUE.asNode()
        val second = INTEGER_VALUE.toString().asNode()
        first.should.equal(second)
    }

    @Test
    fun `leaf nodes with different string content are not equal`() {
        val first = "a".asNode()
        val second = "b".asNode()
        first.should.not.equal(second)
    }

    @Test
    fun toStringIsValue() {
        INT_NODE.toString().should.equal(STRING_WITH_INTEGER_CONTENT)
        STRING_NODE.toString().should.equal(STRING_WITH_NON_INTEGER_CONTENT)
    }

    @Nested
    inner class `when getting leaf node with integer content as string`  : WhenGettingNodeAsString(INT_NODE) {
        @Test
        fun `then result is ok`() = assertValueRetrieved(STRING_WITH_INTEGER_CONTENT)
    }

    @Nested
    inner class `when getting leaf node with non-integer content as string`  : WhenGettingNodeAsString(STRING_NODE) {
        @Test
        fun `then result is ok`() = assertValueRetrieved(STRING_WITH_NON_INTEGER_CONTENT)
    }

    @Nested
    inner class `when getting leaf node with integer content as integer`  : WhenGettingNodeAsInt(INTEGER_VALUE.asNode()) {
        @Test
        fun `then result is ok`() = assertValueRetrieved(INTEGER_VALUE)
    }

    @Nested
    inner class `when getting leaf node with non-integer content as integer`  : WhenGettingNodeAsInt(STRING_NODE) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }


    @Nested
    inner class `when getting leaf node as struct`  : WhenGettingNodeAsStruct(INT_NODE) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting leaf node as list`  : WhenGettingNodeAsList(INT_NODE) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

}