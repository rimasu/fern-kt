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
package com.github.rimasu.text

import org.junit.Test
import kotlin.test.assertEquals


class RegionTest {

    @Test
    fun canGetStartAndEnd() {
        val start = Position(1, 4)
        val end = Position(4, 5)
        val region = Region(start, end)
        assertEquals(start, region.start)
        assertEquals(end, region.end)
    }

    @Test
    fun canConstructDirectly() {
        val start = Position(1, 4)
        val end = Position(4, 5)
        val region = Region(1, 4, 4, 5)
        assertEquals(start, region.start)
        assertEquals(end, region.end)
    }


    @Test
    fun toStringDoesNotRepeatLineIfTheSame() {
        assertEquals("1,10->15",
                Region(
                        Position(1, 10),
                        Position(1, 15)
                ).toString()
        )
    }

    @Test
    fun toStringDoesNotRepeatLineAndPosIfTheSame() {
        assertEquals("1,10",
                Region(
                        Position(1, 10),
                        Position(1, 10)
                ).toString()
        )
    }


    @Test
    fun toStringDoesRepeatLineAndPosIfTheDifferent() {
        assertEquals("1,10->2,15",
                Region(
                        Position(1, 10),
                        Position(2, 15)
                ).toString()
        )
    }
}