package org.rimasu.config

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ConfigListTest {

    val EMPTY = ConfigList()

    @Nested
    inner class `when getting config list as string`  : WhenGettingConfigAsString(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting config list as integer`  : WhenGettingConfigAsInt(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting config list as struct`  : WhenGettingConfigAsStruct(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting config list as list`  : WhenGettingConfigAsList(EMPTY) {

        @Test
        fun `then result is ok`() = assertValueRetrieved(EMPTY)
    }
}