package org.rimasu.node

import com.winterbe.expekt.should
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class IndexStepTest {

    @Nested
    inner class `when index is 1` : WhenIndexIs(1)

    @Nested
    inner class `when index is max int` : WhenIndexIs(Int.MAX_VALUE)

    inner open class WhenIndexIs(private val index: Int) {
        private val step = IndexStep(index)

        @Test
        fun `then to string is hash  followed by index`() {
            step.toString().should.equal("#$index")
        }

        @Test
        fun `then index is index`() {
            step.index.should.equal(index)
        }
    }
}