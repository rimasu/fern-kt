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

@DslMarker
annotation class FernDslMarker

@FernDslMarker
interface ListNodeContent {
    fun add(data: Node)

    fun add(data: String) {
        add(data.asNode())
    }

    fun list(init: ListNodeContent.() -> Unit) {
        val bld = ListNodeBuilder()
        bld.init()
        add(bld.build())
    }

    fun struct(init: StructNodeContent.() -> Unit) {
        val bld = StructNodeBuilder()
        bld.init()
        add( bld.build())
    }
}

@FernDslMarker
interface StructNodeContent {
    fun add(label: String, data: Node)

    fun add(label: String, data: String) {
        add(label, data.asNode())
    }

    fun list(label: String, init: ListNodeContent.() -> Unit) {
        val bld = ListNodeBuilder()
        bld.init()
        add(label, bld.build())
    }

    fun struct(label: String, init: StructNodeContent.() -> Unit) {
        val bld = StructNodeBuilder()
        bld.init()
        add(label, bld.build())
    }
}


object Fern
{
    /**
     * Create a list node using Node DSL.
     */
    @FernDslMarker
    fun list(init: ListNodeContent.() -> Unit) : Node {
        val bld = ListNodeBuilder()
        bld.init()
        return bld.build()
    }

    /**
     * Create a struct node using Node DSL.
     */
    @FernDslMarker
    fun struct(init: StructNodeContent.() -> Unit) : Node {
        val bld = StructNodeBuilder()
        bld.init()
        return bld.build()
    }
}


private class ListNodeBuilder : ListNodeContent {

    private val nodes = mutableListOf<Node>()

    override fun add(data: Node) {
        nodes.add(data)
    }

    fun build() : Node {
        return ListNode(nodes)
    }
}

private class StructNodeBuilder : StructNodeContent {

    private val nodes = mutableMapOf<String, Node>()

    override fun add(label: String, data: Node) {
        nodes[label] = data
    }

    fun build() : Node {
        return StructNode(nodes)
    }
}

