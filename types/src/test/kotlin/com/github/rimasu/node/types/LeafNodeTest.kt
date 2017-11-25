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