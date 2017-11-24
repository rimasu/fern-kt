package org.rimasu.node

import com.winterbe.expekt.should
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.rimasu.fuse.result.assertErr
import org.rimasu.fuse.result.assertOk

class StructNodeTest {

    private val EMPTY = StructNode(emptyMap())

    private val A = "a".asNode()
    private val B = "b".asNode()
    private val C = "c".asNode()

    private val ABC = StructNode(mapOf(
            "a" to A,
            "b" to B,
            "c" to C
    ))

    @Nested
    inner class `when getting struct node as string`  : WhenGettingNodeAsString(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting struct node as integer`  : WhenGettingNodeAsInt(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting struct node as struct`  : WhenGettingNodeAsStruct(EMPTY) {
        @Test
        fun `then result is ok`() = assertValueRetrieved(EMPTY)
    }

    @Nested
    inner class `when getting struct node as list`  : WhenGettingNodeAsList(EMPTY) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }


    @Nested
    inner class `when getting node using undefined key` {

        private val result = ABC["undefined"]

        @Test
        fun `then result is undefined value`() {
            assertErr(result) {
                it.should.be.instanceof(UndefinedValue::class.java)
            }
        }

        @Test
        fun `then result recorded key key`() {
            assertErr(result) {
                if (it is UndefinedValue) {
                    it.key.should.equal("undefined")
                }
            }
        }
    }

    @Nested
    inner class `when getting node using a defined key` {

        private val result = ABC["a"]

        @Test
        fun `then result is ok`() {
            assertOk(result) {
                it.should.equal(A)
            }
        }
    }
}