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
package com.github.rimasu.node.types

import com.winterbe.expekt.should
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith


class AnchorTest
{

    @Test
    fun `exception is thrown if start line is less than one`() {
        assertFailsWith<IllegalArgumentException> {
            Anchor(0, 1, 2, 1)
        }
    }

    @Test
    fun `exception is thrown if start pos is less than one`() {
        assertFailsWith<IllegalArgumentException> {
            Anchor(1, 0, 2, 1)
        }
    }

    @Test
    fun `exception is thrown if end pos is less than one`() {
        assertFailsWith<IllegalArgumentException> {
            Anchor(1, 1, 2, 0)
        }
    }

    @Test
    fun `exception is thrown if end is less than start pos when start line equal end line`() {
        assertFailsWith<IllegalArgumentException> {
            Anchor(2, 2, 2, 1)
        }
    }


    @Test
    fun `exception is thrown if end line is less than start line`() {
        assertFailsWith<IllegalArgumentException> {
            Anchor(3, 1, 2, 1)
        }
    }

    @Test
    fun `can get start end line and pos`() {
        val anchor = Anchor(1,2, 3, 4)
        anchor.startLine.should.equal(1)
        anchor.startPos.should.equal(2)
        anchor.endLine.should.equal(3)
        anchor.endPos.should.equal(4)
    }

    @Test
    fun `to string does not repeat line if the same`() {
        Anchor(1,10,1, 15).toString().should.equal("1,10->15")
    }

    @Test
    fun `to string does not repeat pos line and pos if the same`() {
        Anchor(1,10,1, 10).toString().should.equal("1,10")
    }

    @Test
    fun `to string includes line numbers if the different`() {
        Anchor(1,10,2, 15).toString().should.equal("1,10->2,15")
    }
}