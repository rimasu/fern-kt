package org.rimasu.node

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class NullNodeTest {

    val node = NullNode()

    @Nested
    inner class `when getting null node value as string`  : WhenGettingNodeAsString(node) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()

    }

    @Nested
    inner class `when getting null node value as int`  : WhenGettingNodeAsInt(node) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting null node value as struct`  : WhenGettingNodeAsStruct(node) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting null node value as list`  : WhenGettingNodeAsList(node) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

}

