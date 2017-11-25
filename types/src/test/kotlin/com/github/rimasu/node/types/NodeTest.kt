package com.github.rimasu.node.types

import com.winterbe.expekt.should
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

abstract class NodeTest {

    companion object {
        const val LABEL1 = "label1"
        const val LABEL2 = "label2"
    }

    abstract val node: Node

    @Nested
    inner class `when node does not have a parent` {
        @Test
        fun `then path is empty`() {
            node.path.should.equal(Path(emptyList()))
        }
    }

    @Nested
    inner class `when node has a struct node parent` {

        init {
            StructNode(mapOf(LABEL1 to node))
        }

        @Test
        fun `then path should contain label step`() {
            node.path.should.equal(Path(listOf(LabelStep(LABEL1))))
        }
    }

    @Nested
    inner class `when node has a struct node parent and struct node grand parent` {

        init {
            val parent = StructNode(mapOf(LABEL1 to node))
            StructNode(mapOf(LABEL2 to parent))
        }

        @Test
        fun `then path should contain label steps`() {
            node.path.should.equal(Path(listOf(LabelStep(LABEL2), LabelStep(LABEL1))))
        }
    }

    @Nested
    inner class `when node has a list node parent` {

        init {
            ListNode(listOf(NullNode(), NullNode(), node, NullNode()))
        }

        @Test
        fun `then path should contain index step`() {
            node.path.should.equal(Path(listOf(IndexStep(3))))
        }
    }

    @Nested
    inner class `when node has a list node parent and list node grand parent` {

        init {
            val parent = ListNode(listOf(NullNode(), NullNode(), node, NullNode()))
            ListNode(listOf(NullNode(), parent, NullNode()))
        }

        @Test
        fun `then path should contain index steps`() {
            node.path.should.equal(Path(listOf(IndexStep(2), IndexStep(3))))
        }
    }

    @Nested
    inner class `when node has a list node parent and struct node grand parent` {

        init {
            val parent = ListNode(listOf(NullNode(), NullNode(), node, NullNode()))
            StructNode(mapOf(LABEL2 to parent))
        }

        @Test
        fun `then path should contain index steps`() {
            node.path.should.equal(Path(listOf(LabelStep(LABEL2), IndexStep(3))))
        }
    }
}