package org.rimasu.node

import com.winterbe.expekt.should
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.rimasu.fuse.result.assertErr
import org.rimasu.fuse.result.assertOk

class StructNodeTest : NodeTest() {

    private val EMPTY = StructNode(emptyMap())

    private val A = "a".asNode()
    private val B = "b".asNode()
    private val C = "c".asNode()

    private val ABC = StructNode(mapOf(
            "a" to A,
            "b" to B,
            "c" to C
    ))

    override val node = EMPTY

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
    inner class `when getting node using undefined label` {

        private val result = ABC["undefined"]

        @Test
        fun `then result is undefined value`() {
            assertErr(result) {
                it.should.be.instanceof(UndefinedValue::class.java)
            }
        }

        @Test
        fun `then result recorded path`() {
            assertErr(result) {
                if (it is UndefinedValue) {
                    it.path.should.equal(Path(listOf(LabelStep("undefined"))))
                }
            }
        }
    }

    @Nested
    inner class `when getting node using a defined label` {

        private val result = ABC["a"]

        @Test
        fun `then result is ok`() {
            assertOk(result) {
                it.should.equal(A)
            }
        }
    }
}