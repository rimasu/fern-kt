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

class ListNodeTest : NodeTest() {

    private val A = "a".asNode()
    private val B = "b".asNode()
    private val C = "c".asNode()

    private val EMPTY = ListNode(emptyList())

    private val ABC = ListNode(listOf(A, B, C))

    override val node = EMPTY

    @Test
    fun `can iterate list`() {
        ABC.toList().should.equal(listOf(A, B, C))
    }

    @Test
    fun `to string contains members`() {
        ABC.toString().should.equal("[a b c]")
    }

    @Test
    fun `list nodes with same content are equal`() {
        val first = ListNode(listOf("a".asNode()))
        val second = ListNode(listOf("a".asNode()))
        first.should.equal(second)
    }

    @Test
    fun `list nodes with same content have same hash code`() {
        val first = ListNode(listOf("a".asNode())).hashCode()
        val second = ListNode(listOf("a".asNode())).hashCode()
        first.should.equal(second)
    }

    @Test
    fun `list nodes with different content are not equal`() {
        val first = ListNode(listOf("a".asNode()))
        val second = ListNode(listOf("b".asNode()))
        first.should.not.equal(second)
    }

    @Test
    fun `list nodes with different content are not have different hash codes`() {
        val first = ListNode(listOf("a".asNode())).hashCode()
        val second = ListNode(listOf("b".asNode())).hashCode()
        first.should.not.equal(second)
    }

    @Nested
    inner class `when getting list node as string`  : WhenGettingNodeAsString(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting list node as integer`  : WhenGettingNodeAsInt(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting list node as long`  : WhenGettingNodeAsLong(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting list node as float`  : WhenGettingNodeAsFloat(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting list node as double`  : WhenGettingNodeAsDouble(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting list node as boolean`  : WhenGettingNodeAsBoolean(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting list node as struct`  : WhenGettingNodeAsStruct(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting list node as list`  : WhenGettingNodeAsList(EMPTY) {

        @Test
        fun `then result is ok`() = assertValueRetrieved(EMPTY)
    }

    @Nested
    inner class `when getting node a index zero` {

        private val result = ABC[0]

        @Test
        fun `then result is undefined value`() {
            assertErr(result) {
                it.should.be.instanceof(UndefinedValue::class.java)
            }
        }

        @Test
        fun `then result recorded index is zero`() {
            assertErr(result) {
                if (it is UndefinedValue) {
                    it.path.should.equal(Path(listOf(IndexStep(0))))
                }
            }
        }
    }

    @Nested
    inner class `when getting node a index one` {

        private val result = ABC[1]

        @Test
        fun `then result is ok`() {
            assertOk(result) {
                it.should.equal(A)
            }
        }
    }


    @Nested
    inner class `when getting node a index two` {

        private val result = ABC[2]

        @Test
        fun `then result is ok`() {
            assertOk(result) {
                it.should.equal(B)
            }
        }
    }



    @Nested
    inner class `when getting node a index three` {

        private val result = ABC[3]

        @Test
        fun `then result is ok`() {
            assertOk(result) {
                it.should.equal(C)
            }
        }
    }



    @Nested
    inner class `when getting node a index four` {

        private val result = ABC[4]

        @Test
        fun `then result is undefined value`() {
            assertErr(result) {
                it.should.be.instanceof(UndefinedValue::class.java)
            }
        }

        @Test
        fun `then result recorded index is four`() {
            assertErr(result) {
                if (it is UndefinedValue) {
                    it.path.should.equal(Path(listOf(IndexStep(4))))
                }
            }
        }
    }


}