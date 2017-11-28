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