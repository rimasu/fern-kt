package org.rimasu.fuse.config

import com.danneu.result.flatMap
import com.winterbe.expekt.should
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

import org.rimasu.fuse.result.assertErr
import org.rimasu.fuse.result.assertOk

class MutableConfigTest {

    companion object {
        val KEY1=  "KEY1"
        val STRING_VALUE1= "STRING_VALUE1"
        val INTEGER_VALUE1= 67234123
        val NESTED_VALUE1 = MutableConfig()
    }

    val config = MutableConfig()

    @Nested
    inner class `when undefined value is retrieved as a string` {

        private val value = config[KEY1]

        @Test
        fun theResultIsError() {
            assertErr(value) {
                it.keyPath.should.equal(listOf(KEY1))
            }
        }
    }

    @Nested
    inner class `when data value is retrieved as a string` {

        init { config[KEY1] = STRING_VALUE1 }

        private val value = config[KEY1].flatMap { it.asString() }

        @Test
        fun theResultIsOk() {
            assertOk(value) {
                it.should.equal(STRING_VALUE1)
            }
        }
    }

    @Nested
    inner class `when data value is retrieved as an int` {

        init { config[KEY1] = INTEGER_VALUE1 }

        private val value = config[KEY1].flatMap { it.asInt() }

        @Test
        fun theResultIsOk() {
            assertOk(value) {
                it.should.equal(INTEGER_VALUE1)
            }
        }
    }

    @Nested
    inner class `when data value is retrieved as a badly formatted int` {

        init { config[KEY1] = STRING_VALUE1 }

        private val value = config[KEY1].flatMap { it.asInt() }

        @Test
        fun theResultIsError() {
            assertErr(value) {
                it.keyPath.should.equal(listOf(KEY1))
            }
        }
    }

    @Nested
    inner class `when data value is retrieved as a config` {

        init { config[KEY1] = STRING_VALUE1 }

        private val value = config[KEY1].flatMap { it.asConfig() }

        @Test
        fun theResultIsOk() {
            assertErr(value) {
                it.keyPath.should.equal(listOf(KEY1))
            }
        }
    }

    @Nested
    inner class `when nested value is retrieved as a config` {

        init { config[KEY1] = NESTED_VALUE1 }

        private val value = config[KEY1].flatMap { it.asConfig() }

        @Test
        fun theResultIsOk() {
            assertOk(value) {
                it.should.equal(NESTED_VALUE1)
            }
        }
    }

    @Nested
    inner class `when nested value is retrieved as a string` {

        init { config[KEY1] = NESTED_VALUE1 }

        private val value = config[KEY1].flatMap { it.asString() }

        @Test
        fun theResultIsOk() {
            assertErr(value) {
                it.keyPath.should.equal(listOf(KEY1))
            }
        }
    }

    @Nested
    inner class `when nested value is retrieved as an int` {

        init { config[KEY1] = NESTED_VALUE1 }

        private val value = config[KEY1].flatMap { it.asInt() }

        @Test
        fun theResultIsOk() {
            assertErr(value) {
                it.keyPath.should.equal(listOf(KEY1))
            }
        }
    }
}

