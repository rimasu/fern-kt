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
package com.github.rimasu.fern.types

import org.junit.Test
import kotlin.test.assertEquals

abstract class NodeTest {

    companion object {
        const val LABEL1 = "label1"
        const val LABEL2 = "label2"
    }

    abstract fun createNode(): Node
    private var node: Node
    init {
        node = createNode()
    }

    @Test
    fun aNodeWithNoParentHasAnEmptyPath() {
        assertEquals(Path(emptyList()), node.path)
    }

    @Test
    fun aNodeWithAStructParentHasALabelPath() {
        StructNode(mapOf(LABEL1 to node))
        assertEquals(Path(listOf(
                LabelStep(LABEL1)
        )), node.path)
    }

    @Test
    fun aNodeWithAStructParentAndGrandParentHasATwoStepLabelPath() {

        val parent = StructNode(mapOf(LABEL1 to node))
        StructNode(mapOf(LABEL2 to parent))

        assertEquals(Path(listOf(
                LabelStep(LABEL2),
                LabelStep(LABEL1)
        )), node.path)
    }

    @Test
    fun aNodeWithAListParentHasALabelPath() {
        ListNode(listOf(NullNode(), NullNode(), node, NullNode()))

        assertEquals(Path(listOf(
                IndexStep(3)
        )), node.path)
    }

    @Test
    fun aNodeWithAListParentAndGrandParentHasALabelPath() {
        val parent = ListNode(listOf(NullNode(), NullNode(), node, NullNode()))
        ListNode(listOf(NullNode(), parent, NullNode()))

        assertEquals(Path(listOf(
                IndexStep(2),
                IndexStep(3)
        )), node.path)
    }

    @Test
    fun aNodeWithAListParentAndStructGrandParentHasALabelPath() {
        val parent = ListNode(listOf(NullNode(), NullNode(), node, NullNode()))
        StructNode(mapOf(LABEL2 to parent))

        assertEquals(Path(listOf(
                LabelStep(LABEL2),
                IndexStep(3)
        )), node.path)
    }
}