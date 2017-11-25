package org.rimasu.node

import com.winterbe.expekt.should
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class LabelStepTest {

    @Nested
    inner class `when label is "a"` : WhenIndexIs("a")

    @Nested
    inner class `when label is "azAZ09"` : WhenIndexIs("azAZ09")

    inner open class WhenIndexIs(private val label: String) {
        private val step = LabelStep(label)

        @Test
        fun `then to string is forward slash followed by label`() {
            step.toString().should.equal("/$label")
        }

        @Test
        fun `then label is label`() {
            step.label.should.equal(label)
        }
    }
}