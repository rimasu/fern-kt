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

import com.github.michaelbull.result.expect
import com.github.michaelbull.result.expectError
import com.github.michaelbull.result.getOrElse
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class LeafNodeTest : NodeTest() {

    companion object {
        private val INTEGER_VALUE = 345234
        private val LONG_VALUE = 23423423443L
        private val FLOAT_VALUE = 34.45f
        private val DOUBLE_VALUE = 34.234234234
        private val TRUE_VALUE = "TrUe"
        private val FALSE_VALUE = false
        private val STRING_WITH_INTEGER_CONTENT = INTEGER_VALUE.toString()
        private val STRING_WITH_NON_INTEGER_CONTENT = "Some non-integer text"
        val INT_NODE = STRING_WITH_INTEGER_CONTENT.asNode()
        val STRING_NODE = STRING_WITH_NON_INTEGER_CONTENT.asNode()
    }

    override fun createNode() = STRING_WITH_INTEGER_CONTENT.asNode()

    @Test
    fun leafNodesWithSameStringContentAreEqual() {
        val first = INTEGER_VALUE.asNode()
        val second = INTEGER_VALUE.toString().asNode()
        assertEquals(first, second)
    }

    @Test
    fun leafNodesWithDifferentStringContentNotAreEqual() {
        val first = STRING_WITH_NON_INTEGER_CONTENT.asNode()
        val second = INTEGER_VALUE.toString().asNode()
        assertNotEquals(first, second)
    }

    @Test
    fun toStringIsValue() {
        assertEquals(STRING_WITH_INTEGER_CONTENT, INT_NODE.toString())
        assertEquals(STRING_WITH_NON_INTEGER_CONTENT, STRING_NODE.toString())
    }

    @Test
    fun gettingLeafNodeWithIntegerContentAsStringWorks() {
        assertEquals(STRING_WITH_INTEGER_CONTENT, INT_NODE.asString().getOrElse { fail() })
    }

    @Test
    fun gettingIntegerLeafNodeWithMinValueFailsIfValueIsTooLow() {
        val error = "32".asNode().asInt(min=33).expectError { fail() }
        assertTrue(error is InvalidLowValue)
    }

    @Test
    fun gettingIntegerLeafNodeWithMinValueWorksIfValueIsNotTooLow() {
        val value = "33".asNode().asInt(min=33).expect{ fail() }
        assertEquals(33, value)
    }

    @Test
    fun gettingIntegerLeafNodeWithMaxValueFailsIfValueIsTooHigh() {
        val error = "32".asNode().asInt(max=31).expectError { fail() }
        assertTrue(error is InvalidHighValue)
    }

    @Test
    fun gettingIntegerLeafNodeWithMaxValueWorksIfValueIsNotTooHigh() {
        val value = "31".asNode().asInt(max=31).expect{ fail() }
        assertEquals(31, value)
    }

    @Test
    fun gettingLeafNodeWithNonIntegerContentAsStringWorks() {
        assertEquals(STRING_WITH_NON_INTEGER_CONTENT, STRING_NODE.asString().getOrElse { fail() })
    }

    @Test
    fun gettingLeafNodeWithIntegerContentAsIntegerWorks() {
        assertEquals(INTEGER_VALUE, INTEGER_VALUE.asNode().asInt().getOrElse { fail() })
    }

    @Test
    fun gettingLeafNodeWithNonIntegerContentAsIntegerFails() {
        val error = STRING_NODE.asInt().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingLeafNodeWithLongContentAsLongWorks() {
        assertEquals(LONG_VALUE, LONG_VALUE.asNode().asLong().getOrElse { fail() })
    }

    @Test
    fun gettingLeafNodeWithNonLongContentAsLongFails() {
        val error = STRING_NODE.asLong().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingLongLeafNodeWithMinValueFailsIfValueIsTooLow() {
        val error = "32".asNode().asLong(min=33).expectError { fail() }
        assertTrue(error is InvalidLowValue)
    }

    @Test
    fun gettingLongLeafNodeWithMinValueWorksIfValueIsNotTooLow() {
        val value = "33".asNode().asLong(min=33).expect{ fail() }
        assertEquals(33, value)
    }

    @Test
    fun gettingLongLeafNodeWithMaxValueFailsIfValueIsTooHigh() {
        val error = "32".asNode().asLong(max=31).expectError { fail() }
        assertTrue(error is InvalidHighValue)
    }

    @Test
    fun gettingLongLeafNodeWithMaxValueWorksIfValueIsNotTooHigh() {
        val value = "31".asNode().asLong(max=31).expect{ fail() }
        assertEquals(31, value)
    }

    @Test
    fun gettingLeafNodeWithFloatContentAsFloatWorks() {
        assertEquals(FLOAT_VALUE, FLOAT_VALUE.asNode().asFloat().getOrElse { fail() })
    }

    @Test
    fun gettingLeafNodeWithNonFloatContentAsFloatFails() {
        val error = STRING_NODE.asFloat().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingFloatLeafNodeWithMinValueFailsIfValueIsTooLow() {
        val error = "32".asNode().asFloat(min=33.0f).expectError { fail() }
        assertTrue(error is InvalidLowValue)
    }

    @Test
    fun gettingFloatLeafNodeWithMinValueWorksIfValueIsNotTooLow() {
        val value = "33".asNode().asFloat(min=33.0f).expect{ fail() }
        assertEquals(33.0f, value)
    }

    @Test
    fun gettingFloatLeafNodeWithMaxValueFailsIfValueIsTooHigh() {
        val error = "32".asNode().asFloat(max=31.0f).expectError { fail() }
        assertTrue(error is InvalidHighValue)
    }

    @Test
    fun gettingFloatLeafNodeWithMaxValueWorksIfValueIsNotTooHigh() {
        val value = "31".asNode().asFloat(max=31.0f).expect{ fail() }
        assertEquals(31.0f, value)
    }

    @Test
    fun gettingLeafNodeWithDoubleContentAsDoubleWorks() {
        assertEquals(DOUBLE_VALUE, DOUBLE_VALUE.asNode().asDouble().getOrElse { fail() })
    }

    @Test
    fun gettingLeafNodeWithNonDoubleContentAsDoubleFails() {
        val error = STRING_NODE.asDouble().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingDoubleLeafNodeWithMinValueFailsIfValueIsTooLow() {
        val error = "32".asNode().asDouble(min=33.0).expectError { fail() }
        assertTrue(error is InvalidLowValue)
    }

    @Test
    fun gettingDoubleLeafNodeWithMinValueWorksIfValueIsNotTooLow() {
        val value = "33".asNode().asDouble(min=33.0).expect{ fail() }
        assertEquals(33.0, value)
    }

    @Test
    fun gettingDoubleLeafNodeWithMaxValueFailsIfValueIsTooHigh() {
        val error = "32".asNode().asDouble(max=31.0).expectError { fail() }
        assertTrue(error is InvalidHighValue)
    }

    @Test
    fun gettingDoubleLeafNodeWithMaxValueWorksIfValueIsNotTooHigh() {
        val value = "31".asNode().asDouble(max=31.0).expect{ fail() }
        assertEquals(31.0, value)
    }

    @Test
    fun gettingLeafNodeWithTrueContentAsBooleanWorks() {
        assertEquals(true, TRUE_VALUE.asNode().asBoolean().getOrElse { fail() })
    }

    @Test
    fun gettingLeafNodeWithFalseContentAsBooleanWorks() {
        assertEquals(false, FALSE_VALUE.asNode().asBoolean().getOrElse { fail() })
    }

    @Test
    fun gettingLeafNodeWithNonBooleanContentAsBooleanFails() {
        val error = STRING_NODE.asBoolean().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingLeafNodeAsStructFails() {
        val error = STRING_NODE.asStruct().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }

    @Test
    fun gettingLeafNodeAsListFails() {
        val error = STRING_NODE.asList().expectError { fail() }
        assertTrue(error is IncompatibleValue)
    }
}