/**
 * Copyright 2017 Richard Sunderland
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *     DEALINGS IN THE SOFTWARE.
 */
package com.github.rimasu.node.types

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

    @Nested
    inner class `when step added to path` {

        private val stepA = LabelStep("a")
        private val initial = Path(listOf(stepA))
        private val next = LabelStep("b")
        private val combined = initial + next

        @Test
        fun `then combined contains all steps`() {
            combined.should.equal(Path(listOf(stepA, next)))
        }

        @Test
        fun `then initial path is unchanged`() {
            initial.should.equal(Path(listOf(stepA)))
        }
    }
}