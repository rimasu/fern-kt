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