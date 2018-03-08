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
import kotlin.test.*

class StructNodeTest : NodeTest() {

    private companion object {
        private val EMPTY = StructNode(emptyMap())

        private val A = "a".asNode()
        private val B = "b".asNode()
        private val C = "c".asNode()

        private val ABC = StructNode(mapOf(
                "a" to A,
                "b" to B,
                "c" to C
        ))
    }

    override fun createNode() = StructNode(emptyMap())

    @Test
    fun toStringContainsMembers() {
        assertEquals("{a=a b=b c=c}", ABC.toString())
    }

    @Test
    fun structNodesWithSameContentAreEqual() {
        val first = StructNode(mapOf("a" to "a".asNode()))
        val second = StructNode(mapOf("a" to "a".asNode()))
        assertEquals(first, second)
    }

    @Test
    fun structNodesWithSameContentHaveSameHashCode() {
        val first = StructNode(mapOf("a" to "a".asNode())).hashCode()
        val second = StructNode(mapOf("a" to "a".asNode())).hashCode()
        assertEquals(first, second)
    }

    @Test
    fun structNodesWithDifferentContentAreNotEqual() {
        val first = StructNode(mapOf("a" to "b".asNode()))
        val second = StructNode(mapOf("a" to "a".asNode()))
        assertNotEquals(first, second)
    }

    @Test
    fun structNodesWithDifferentContentHaveDifferentHashCode() {
        val first = StructNode(mapOf("a" to "b".asNode())).hashCode()
        val second = StructNode(mapOf("a" to "a".asNode())).hashCode()
        assertNotEquals(first, second)
    }

    @Test
    fun gettingStructNodeAsStringFails() {
        val error = EMPTY.asString().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingStructNodeAsIntegerFails() {
        val error = EMPTY.asInt().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingStructNodeAsLongFails() {
        val error = EMPTY.asLong().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingStructNodeAsFloatFails() {
        val error = EMPTY.asFloat().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingStructNodeAsDoubleFails() {
        val error = EMPTY.asDouble().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingStructNodeAsBooleanFails() {
        val error = EMPTY.asBoolean().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingStructNodeAsListFails() {
        val error = EMPTY.asList().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingStructNodeAsStructWorks() {
        assertEquals(EMPTY, EMPTY.asStruct().expect { fail() })
    }

    @Test
    fun gettingUndefinedMemberFails() {
        val result = ABC["undefined"].expectError { fail() } as UndefinedValue
        assertEquals(Path(listOf(LabelStep("undefined"))), result.path)
    }

    @Test
    fun gettingDefinedMemberWorks() {
        assertEquals(A, ABC["a"].expect { fail() })
    }

    @Test
    fun gettingOptionalUndefinedReturnsNull() {
        assertNull(ABC.getOptional("UNDEFINED"))
    }

    @Test
    fun gettingOptionalDefinedReturnsValue() {
        assertEquals(A, ABC.getOptional("a"))
    }
}