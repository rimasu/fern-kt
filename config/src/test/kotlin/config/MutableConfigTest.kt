package config

import assertErr
import assertOk
import com.winterbe.expekt.should
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.rimasu.fuse.config.MutableConfig

class MutableConfigTest {

    companion object {
        val KEY1=  "KEY1"
        val STRING_VALUE1= "STRING_VALUE1"
    }

    val config = MutableConfig()

    @Nested
    inner class WhenUndefinedStringIsRetrieved {

        private val value = config[KEY1].map { it.asString() }

        @Test
        fun theResultIsError() {
            assertErr(value) {
                it.keyPath.should.equal(listOf(KEY1))
            }
        }
    }



    @Nested
    inner class WhenDefinedStringIsRetrieved {

        init {
            config[KEY1] = STRING_VALUE1
        }

        private val value = config[KEY1].map { it.asString() }

        @Test
        fun theResultIsOkString() {
            assertOk(value) {
                println(it)
            }
        }
    }
}

