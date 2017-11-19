package org.rimasu.config

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class NullConfigTest {

    val config = NullConfig()

    @Nested
    inner class `when getting null config value as string`  : WhenGettingConfigAsString(config) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()

    }

    @Nested
    inner class `when getting null config value as int`  : WhenGettingConfigAsInt(config) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting null config value as struct`  : WhenGettingConfigAsStruct(config) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting null config value as list`  : WhenGettingConfigAsList(config) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

}

