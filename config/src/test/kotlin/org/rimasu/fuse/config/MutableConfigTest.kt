package org.rimasu.fuse.config

import assertErr
import com.winterbe.expekt.should
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MutableConfigTest {

    companion object {
        val KEY1=  "KEY1"
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
}

