package org.rimasu.node

import com.winterbe.expekt.should
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.rimasu.fuse.result.assertErr
import org.rimasu.fuse.result.assertOk

class ListNodeTest : NodeTest() {

    private val A = "a".asNode()
    private val B = "b".asNode()
    private val C = "c".asNode()

    private val EMPTY = ListNode(emptyList())

    private val ABC = ListNode(listOf(A, B, C))

    override val node = EMPTY

    @Test
    fun `can iterate list`() {
        ABC.toList().should.equal(listOf(A, B, C))
    }


    @Nested
    inner class `when getting list node as string`  : WhenGettingNodeAsString(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting list node as integer`  : WhenGettingNodeAsInt(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting list node as struct`  : WhenGettingNodeAsStruct(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting list node as list`  : WhenGettingNodeAsList(EMPTY) {

        @Test
        fun `then result is ok`() = assertValueRetrieved(EMPTY)
    }

    @Nested
    inner class `when getting node a index zero` {

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
    inner class `when getting node a index one` {

        private val result = ABC[1]

        @Test
        fun `then result is ok`() {
            assertOk(result) {
                it.should.equal(A)
            }
        }
    }


    @Nested
    inner class `when getting node a index two` {

        private val result = ABC[2]

        @Test
        fun `then result is ok`() {
            assertOk(result) {
                it.should.equal(B)
            }
        }
    }



    @Nested
    inner class `when getting node a index three` {

        private val result = ABC[3]

        @Test
        fun `then result is ok`() {
            assertOk(result) {
                it.should.equal(C)
            }
        }
    }



    @Nested
    inner class `when getting node a index four` {

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