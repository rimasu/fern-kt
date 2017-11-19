package org.rimasu.config

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
        val LIST_VALUE1 : Collection<ConfigValue> = mutableListOf(DataConfigValue(STRING_VALUE1), NestedConfigValue(NESTED_VALUE1))
    }

    val config = MutableConfig()

    @Nested
    inner class `when undefined value is retrieved as a string` {

        private val value = config[KEY1]

        @Test
        fun `then result is error`() {
            assertErr(value) {
                it.should.be.instanceof(UndefinedValue::class.java)
            }
        }
    }

    @Nested
    inner class `when data value is retrieved as a string` {

        init { config[KEY1] = STRING_VALUE1
        }

        private val value = config[KEY1].flatMap { it.asString() }

        @Test
        fun `then result is ok`() {
            assertOk(value) {
                it.should.equal(STRING_VALUE1)
            }
        }
    }

    @Nested
    inner class `when data value is retrieved as an int` {

        init { config[KEY1] = INTEGER_VALUE1
        }

        private val value = config[KEY1].flatMap { it.asInt() }

        @Test
        fun `then result is error`() {
            assertOk(value) {
                it.should.equal(INTEGER_VALUE1)
            }
        }
    }

    @Nested
    inner class `when data value is retrieved as a badly formatted int` {

        init { config[KEY1] = STRING_VALUE1
        }

        private val value = config[KEY1].flatMap { it.asInt() }

        @Test
        fun `then result is error`() {
            assertErr(value) {
                it.should.be.instanceof(IncompatibleValue::class.java)
            }
        }
    }

    @Nested
    inner class `when data value is retrieved as a config` {

        init { config[KEY1] = STRING_VALUE1
        }

        private val value = config[KEY1].flatMap { it.asConfig() }

        @Test
        fun `then result is error`() {
            assertErr(value) {
                it.should.be.instanceof(IncompatibleValue::class.java)
            }
        }
    }

    @Nested
    inner class `when data value is retrieved as a list` {

        init { config[KEY1] = STRING_VALUE1
        }

        private val value = config[KEY1].flatMap { it.asList() }

        @Test
        fun `then result is error`() {
            assertErr(value) {
                it.should.be.instanceof(IncompatibleValue::class.java)
            }
        }
    }

    @Nested
    inner class `when nested value is retrieved as a config` {

        init { config[KEY1] = NESTED_VALUE1
        }

        private val value = config[KEY1].flatMap { it.asConfig() }

        @Test
        fun `then result is error`() {
            assertOk(value) {
                it.should.equal(NESTED_VALUE1)
            }
        }
    }

    @Nested
    inner class `when nested value is retrieved as a string` {

        init { config[KEY1] = NESTED_VALUE1
        }

        private val value = config[KEY1].flatMap { it.asString() }

        @Test
        fun `then result is error`() {
            assertErr(value) {
                it.should.be.instanceof(IncompatibleValue::class.java)
            }
        }
    }

    @Nested
    inner class `when nested value is retrieved as an int` {

        init { config[KEY1] = NESTED_VALUE1
        }

        private val value = config[KEY1].flatMap { it.asInt() }

        @Test
        fun `then result is error`() {
            assertErr(value) {
                it.should.be.instanceof(IncompatibleValue::class.java)
            }
        }
    }

    @Nested
    inner class `when nested value is retrieved as an list` {

        init { config[KEY1] = NESTED_VALUE1
        }

        private val value = config[KEY1].flatMap { it.asList() }

        @Test
        fun `then result is error`() {
            assertErr(value) {
                it.should.be.instanceof(IncompatibleValue::class.java)
            }
        }
    }

    @Nested
    inner class `when list value is retrieved as a string` {

        init { config[KEY1] = LIST_VALUE1
        }

        private val value = config[KEY1].flatMap { it.asString() }

        @Test
        fun `then result is error`() {
            assertErr(value) {
                it.should.be.instanceof(IncompatibleValue::class.java)
            }
        }
    }

    @Nested
    inner class `when list value is retrieved as an int` {

        init { config[KEY1] = LIST_VALUE1
        }

        private val value = config[KEY1].flatMap { it.asInt() }

        @Test
        fun `then result is error`() {
            assertErr(value) {
                it.should.be.instanceof(IncompatibleValue::class.java)
            }
        }
    }

    @Nested
    inner class `when list value is retrieved as an config` {

        init { config[KEY1] = LIST_VALUE1
        }

        private val value = config[KEY1].flatMap { it.asConfig() }

        @Test
        fun `then result is error`() {
            assertErr(value) {
                it.should.be.instanceof(IncompatibleValue::class.java)
            }
        }
    }

    @Nested
    inner class `when list value is retrieved as an list` {

        init { config[KEY1] = LIST_VALUE1
        }

        private val value = config[KEY1].flatMap { it.asList() }

        @Test
        fun `then result is ok`() {
            assertOk(value) {
                it.should.equal(LIST_VALUE1)
            }
        }
    }
}

