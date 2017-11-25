package org.rimasu.node

import com.winterbe.expekt.should
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PathTest {

    @Nested
    inner class `when path is empty` {
        private val path = Path(emptyList())

        @Test
        fun `then to string is empty braces`() {
            path.toString().should.equal("{}")
        }

        @Test
        fun `then steps should be empty`() {
            path.steps.size.should.equal(0)
        }
    }

    @Nested
    inner class `when path has multiple steps` {
        private val path = Path(listOf(
                LabelStep("a"),
                LabelStep("b"),
                IndexStep(3),
                IndexStep(100),
                LabelStep("c"),
                IndexStep(7)
        ))

        @Test
        fun `then to string is steps to string in braces`() {
            path.toString().should.equal("{/a/b#3#100/c#7}")
        }
    }
}