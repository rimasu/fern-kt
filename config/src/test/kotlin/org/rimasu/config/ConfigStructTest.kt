package org.rimasu.config

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ConfigStructTest {

    val EMPTY = ConfigStruct()

    @Nested
    inner class `when getting config struct as string`  : WhenGettingConfigAsString(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting config struct as integer`  : WhenGettingConfigAsInt(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting config struct as struct`  : WhenGettingConfigAsStruct(EMPTY) {
        @Test
        fun `then result is ok`() = assertValueRetrieved(EMPTY)
    }

    @Nested
    inner class `when getting config struct as list`  : WhenGettingConfigAsList(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }
}