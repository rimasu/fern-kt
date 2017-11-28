package com.github.rimasu.text

import com.winterbe.expekt.should
import org.junit.jupiter.api.Test

class RegionTest {

    @Test
    fun `can get line and column`() {
        val start = Position(1,4)
        val end = Position(4, 5)
        val region = Region(start, end)
        region.start.should.equal(start)
        region.end.should.equal(end)
    }

    @Test
    fun `to string does not repeat line if the same`() {
        Region(
                Position(1,10),
                Position(1, 15)
        ).toString().should.equal("1,10->15")
    }

    @Test
    fun `to string does not repeat pos line and pos if the same`() {
        Region(
                Position(1,10),
                Position(1, 10)
        ).toString().should.equal("1,10")
    }

    @Test
    fun `to string includes line numbers if the different`() {
        Region(
                Position(1,10),
                Position(2, 15)
        ).toString().should.equal("1,10->2,15")
    }
}