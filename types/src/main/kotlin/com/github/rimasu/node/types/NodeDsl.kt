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

@DslMarker
annotation class NodeDslMarker

@NodeDslMarker
interface ListNodeContent {
    fun add(data: Node)

    fun add(data: String) {
        add(data.asNode())
    }
}

@NodeDslMarker
interface StructNodeContent {
    fun add(label: String, data: Node)

    fun add(label: String, data: String) {
        add(label, data.asNode())
    }
}

/**
 * Create a list node using Node DSL.
 */
@NodeDslMarker
fun listNode(init: ListNodeContent.() -> Unit) : Node {
    val bld = ListNodeBuilder()
    bld.init()
    return bld.build()
}

/**
 * Create a struct node using Node DSL.
 */
@NodeDslMarker
fun structNode(init: StructNodeContent.() -> Unit) : Node {
    val bld = StructNodeBuilder()
    bld.init()
    return bld.build()
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

