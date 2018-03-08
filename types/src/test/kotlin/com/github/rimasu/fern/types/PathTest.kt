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
 * DEALINGS IN THE SOFTWARE.
 */
package com.github.rimasu.fern.types

import org.junit.Test
import kotlin.test.assertEquals

class PathTest {

    @Test
    fun toStringOfEmptyPathIsEmptyBraces() {
        assertEquals("{}", Path(emptyList()).toString())
    }

    @Test
    fun toStringOfPopulatedPathIsStepsInBraces() {
        val path = Path(listOf(
                LabelStep("a"),
                LabelStep("b"),
                IndexStep(3),
                IndexStep(100),
                LabelStep("c"),
                IndexStep(7)
        ))

        assertEquals("{/a/b#3#100/c#7}", path.toString())
    }

    @Test
    fun canCreatePathByAddingAStepToAnExistingPath() {
        val stepA = LabelStep("a")
        val initial = Path(listOf(stepA))
        val next = LabelStep("b")
        val combined = initial + next

        assertEquals(Path(listOf(stepA, next)), combined)

        // initial unaffected
        assertEquals(Path(listOf(stepA)), initial)
    }
}