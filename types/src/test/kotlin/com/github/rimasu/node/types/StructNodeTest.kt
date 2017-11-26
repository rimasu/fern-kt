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

class StructNodeTest : NodeTest() {

    private val EMPTY = StructNode(emptyMap())

    private val A = "a".asNode()
    private val B = "b".asNode()
    private val C = "c".asNode()

    private val ABC = StructNode(mapOf(
            "a" to A,
            "b" to B,
            "c" to C
    ))

    override val node = EMPTY

    @Test
    fun `to string contains members`() {
        ABC.toString().should.equal("{a=a b=b c=c}")
    }

    @Test
    fun `struct nodes with same content are equal`() {
        val first = StructNode(mapOf("a" to "a".asNode()))
        val second = StructNode(mapOf("a" to "a".asNode()))
        first.should.equal(second)
    }


    @Test
    fun `struct nodes with same content have same hash code`() {
        val first = StructNode(mapOf("a" to "a".asNode())).hashCode()
        val second = StructNode(mapOf("a" to "a".asNode())).hashCode()
        first.should.equal(second)
    }

    @Test
    fun `struct nodes with different content are not equal`() {
        val first = StructNode(mapOf("a" to "b".asNode()))
        val second = StructNode(mapOf("a" to "a".asNode()))
        first.should.not.equal(second)
    }

    @Test
    fun `struct nodes with different content have different hash codes`() {
        val first = StructNode(mapOf("a" to "b".asNode())).hashCode()
        val second = StructNode(mapOf("a" to "a".asNode())).hashCode()
        first.should.not.equal(second)
    }

    @Nested
    inner class `when getting struct node as string`  : WhenGettingNodeAsString(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting struct node as integer`  : WhenGettingNodeAsInt(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting struct node as struct`  : WhenGettingNodeAsStruct(EMPTY) {
        @Test
        fun `then result is ok`() = assertValueRetrieved(EMPTY)
    }

    @Nested
    inner class `when getting struct node as list`  : WhenGettingNodeAsList(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }


    @Nested
    inner class `when getting node using undefined label` {

        private val result = ABC["undefined"]

        @Test
        fun `then result is undefined value`() {
            assertErr(result) {
                it.should.be.instanceof(UndefinedValue::class.java)
            }
        }

        @Test
        fun `then result recorded path`() {
            assertErr(result) {
                if (it is UndefinedValue) {
                    it.path.should.equal(Path(listOf(LabelStep("undefined"))))
                }
            }
        }
    }

    @Nested
    inner class `when getting node using a defined label` {

        private val result = ABC["a"]

        @Test
        fun `then result is ok`() {
            assertOk(result) {
                it.should.equal(A)
            }
        }
    }
}