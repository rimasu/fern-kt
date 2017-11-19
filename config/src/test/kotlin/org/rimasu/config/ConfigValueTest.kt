package org.rimasu.config

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ConfigValueTest {

    companion object {
        private val INTEGER_VALUE = 345234
        private val STRING_WITH_INTEGER_CONTENT = INTEGER_VALUE.toString()
        private val STRING_WITH_NON_INTEGER_CONTENT = "Some non-integer text"
    }

    val INT_CONFIG = STRING_WITH_INTEGER_CONTENT.asConfigValue()
    val STRING_CONFIG = STRING_WITH_NON_INTEGER_CONTENT.asConfigValue()

    @Nested
    inner class `when getting config value with integer content as string`  : WhenGettingConfigAsString(INT_CONFIG) {
        @Test
        fun `then result is ok`() = assertValueRetrieved(STRING_WITH_INTEGER_CONTENT)
    }

    @Nested
    inner class `when getting config value with non-integer content as string`  : WhenGettingConfigAsString(STRING_CONFIG) {
        @Test
        fun `then result is ok`() = assertValueRetrieved(STRING_WITH_NON_INTEGER_CONTENT)
    }

    @Nested
    inner class `when getting config value with integer content as integer`  : WhenGettingConfigAsInt(INTEGER_VALUE.asConfigValue()) {
        @Test
        fun `then result is ok`() = assertValueRetrieved(INTEGER_VALUE)
    }

    @Nested
    inner class `when getting config value with non-integer content as integer`  : WhenGettingConfigAsInt(STRING_CONFIG) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }


    @Nested
    inner class `when getting config value as struct`  : WhenGettingConfigAsStruct(INT_CONFIG) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting config value as list`  : WhenGettingConfigAsList(INT_CONFIG) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

}