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

import com.github.michaelbull.result.expectError
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class NullNodeTest : NodeTest() {
    
    companion object {
        private val NULL_NODE = NullNode()
    }

    override fun createNode() = NullNode()

    @Test
    fun toStringEqualsUnderscore() {
        assertEquals("_", NullNode().toString())
    }
    
    @Test
    fun nullNodesAreEqual() {
        val first = NullNode()
        val second = NullNode()
        assertEquals(first, second)
    }

    @Test
    fun nullNodesHaveSameHashCode() {
        val first = NullNode().hashCode()
        val second = NullNode().hashCode()
        assertEquals(first, second)
    }

    @Test
    fun gettingNullNodeAsStringFails() {
        val error = NULL_NODE.asString().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingNullNodeAsIntegerFails() {
        val error = NULL_NODE.asInt().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingNullNodeAsLongFails() {
        val error = NULL_NODE.asLong().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingNullNodeAsFloatFails() {
        val error = NULL_NODE.asFloat().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingNullNodeAsDoubleFails() {
        val error = NULL_NODE.asDouble().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingNullNodeAsBooleanFails() {
        val error = NULL_NODE.asBoolean().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingNullNodeAsListFails() {
        val error = NULL_NODE.asList().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingNullNodeAsStructWorks() {
        val error = NULL_NODE.asStruct().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }
}

