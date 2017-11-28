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

import com.winterbe.expekt.should
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class PositionTest {

    @Test
    fun `to string includes line and position`() {
        Position(3, 5).toString().should.equal("3,5")
    }

    @Test
    fun `exception is thrown if line is less than one`() {
        assertFailsWith<IllegalArgumentException> {
            Position(0, 1)
        }
    }

    @Test
    fun `exception is thrown if column is less than one`() {
        assertFailsWith<IllegalArgumentException> {
            Position(1, 0)
        }
    }

    @Test
    fun `can get line and column`() {
        Position(3, 5).line.should.equal(3)
        Position(3, 5).column.should.equal(5)
    }
}