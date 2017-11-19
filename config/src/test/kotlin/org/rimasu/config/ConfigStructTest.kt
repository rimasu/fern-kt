package org.rimasu.config

import com.winterbe.expekt.should
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.rimasu.fuse.result.assertErr
import org.rimasu.fuse.result.assertOk

class ConfigStructTest {

    private val EMPTY = ConfigStruct(emptyMap())

    private val A = "a".asConfigValue()
    private val B = "b".asConfigValue()
    private val C = "c".asConfigValue()

    private val ABC = ConfigStruct(mapOf(
            "a" to A,
            "b" to B,
            "c" to C
    ))

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


    @Nested
    inner class `when getting config using undefined key` {

        private val result = ABC["undefined"]

        @Test
        fun `then result is undefined value`() {
            assertErr(result) {
                it.should.be.instanceof(UndefinedValue::class.java)
            }
        }

        @Test
        fun `then result recorded key key`() {
            assertErr(result) {
                if (it is UndefinedValue) {
                    it.key.should.equal("undefined")
                }
            }
        }
    }

    @Nested
    inner class `when getting config using a defined key` {

        private val result = ABC["a"]

        @Test
        fun `then result is ok`() {
            assertOk(result) {
                it.should.equal(A)
            }
        }
    }
}