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
package com.github.rimasu.fern.types

import com.github.michaelbull.result.expect
import com.github.michaelbull.result.expectError
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class ListNodeTest : NodeTest() {

    private companion object {
        private val A = "a".asNode()
        private val B = "b".asNode()
        private val C = "c".asNode()

        private val EMPTY = ListNode(emptyList())

        private val ABC = ListNode(listOf(A, B, C))
    }

    override fun createNode() = ListNode(emptyList())

    @Test
    fun canIterateList() {
        assertEquals(listOf(A, B, C), ABC.toList())
    }

    @Test
    fun toStringContainsMembers() {
        assertEquals("[a b c]", ABC.toString())
    }

    @Test
    fun listNodesWithSameContentAreEqual() {
        val first = ListNode(listOf("a".asNode()))
        val second = ListNode(listOf("a".asNode()))
        assertEquals(first, second)
    }

    @Test
    fun listNodesWithSameContentHaveSameHashCode() {
        val first = ListNode(listOf("a".asNode())).hashCode()
        val second = ListNode(listOf("a".asNode())).hashCode()
        assertEquals(first, second)
    }

    @Test
    fun listNodesWithDifferentContentAreNotEqual() {
        val first = ListNode(listOf("a".asNode()))
        val second = ListNode(listOf("b".asNode()))
        assertNotEquals(first, second)
    }

    @Test
    fun listNodesWithDifferentContentHaveDifferentHashCode() {
        val first = ListNode(listOf("a".asNode())).hashCode()
        val second = ListNode(listOf("b".asNode())).hashCode()
        assertNotEquals(first, second)
    }

    @Test
    fun gettingListNodeAsStringFails() {
        val error = EMPTY.asString().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingListNodeAsIntegerFails() {
        val error = EMPTY.asInt().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingListNodeAsLongFails() {
        val error = EMPTY.asLong().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingListNodeAsFloatFails() {
        val error = EMPTY.asFloat().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingListNodeAsDoubleFails() {
        val error = EMPTY.asDouble().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingListNodeAsBooleanFails() {
        val error = EMPTY.asBoolean().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingListNodeAsListWorks() {
        assertEquals(EMPTY, EMPTY.asList().expect { fail() })
    }

    @Test
    fun gettingListNodeAsStructWorks() {
        val error = EMPTY.asStruct().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingNodeAtIndexZeroFails() {
        val error = EMPTY[0].expectError { fail() } as UndefinedValue
        assertEquals(Path(listOf(IndexStep(0))), error.path)
    }

    @Test
    fun gettingNodesAtValidIndexesWorks() {
        assertEquals(A, ABC[1].expect { fail() })
        assertEquals(B, ABC[2].expect { fail() })
        assertEquals(C, ABC[3].expect { fail() })
    }

    @Test
    fun gettingNodeAtTooHighFails() {
        val error = EMPTY[4].expectError { fail() } as UndefinedValue
        assertEquals(Path(listOf(IndexStep(4))), error.path)
    }
}