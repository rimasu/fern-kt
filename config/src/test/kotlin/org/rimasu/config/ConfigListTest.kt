package org.rimasu.config

import com.winterbe.expekt.should
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.rimasu.fuse.result.assertErr
import org.rimasu.fuse.result.assertOk

class ConfigListTest {

    private val A = "a".asConfigValue()
    private val B = "b".asConfigValue()
    private val C = "c".asConfigValue()

    private val EMPTY = ConfigList(emptyList())

    private val ABC = ConfigList(listOf(A, B, C))

    @Test
    fun canIterateList() {
        ABC.toList().should.equal(listOf(A, B, C))
    }


    @Nested
    inner class `when getting config list as string`  : WhenGettingConfigAsString(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting config list as integer`  : WhenGettingConfigAsInt(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting config list as struct`  : WhenGettingConfigAsStruct(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting config list as list`  : WhenGettingConfigAsList(EMPTY) {

        @Test
        fun `then result is ok`() = assertValueRetrieved(EMPTY)
    }

    @Nested
    inner class `when getting config a index zero` {

        private val result = ABC[0]

        @Test
        fun `then result is invalid index`() {
            assertErr(result) {
                it.should.be.instanceof(InvalidIndex::class.java)
            }
        }

        @Test
        fun `then result recorded index is zero`() {
            assertErr(result) {
                if (it is InvalidIndex) {
                    it.index.should.equal(0)
                }
            }
        }
    }

    @Nested
    inner class `when getting config a index one` {

        private val result = ABC[1]

        @Test
        fun `then result is ok`() {
            assertOk(result) {
                it.should.equal(A)
            }
        }
    }


    @Nested
    inner class `when getting config a index two` {

        private val result = ABC[2]

        @Test
        fun `then result is ok`() {
            assertOk(result) {
                it.should.equal(B)
            }
        }
    }



    @Nested
    inner class `when getting config a index three` {

        private val result = ABC[3]

        @Test
        fun `then result is ok`() {
            assertOk(result) {
                it.should.equal(C)
            }
        }
    }



    @Nested
    inner class `when getting config a index four` {

        private val result = ABC[4]

        @Test
        fun `then result is invalid index`() {
            assertErr(result) {
                it.should.be.instanceof(InvalidIndex::class.java)
            }
        }

        @Test
        fun `then result recorded index is four`() {
            assertErr(result) {
                if (it is InvalidIndex) {
                    it.index.should.equal(4)
                }
            }
        }
    }


}