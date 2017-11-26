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

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok

/** A piece of node */
sealed class Node {

    private var parent: Node? = null
    private var usage: Step? = null

    /** Get the value as a string. Will return an error if the value can not be coerced into a string. */
    open fun asString(): Result<String, NodeError> = incompatibleValue(path)

    /** Get the value as an integer. Will return an error if the value can not be coerced into a int. */
    open fun asInt(): Result<Int, NodeError> =  incompatibleValue(path)

    /** Get the value as node. Will return an error if the value can not be coerced into a node. */
    open fun asStruct(): Result<StructNode, NodeError> = incompatibleValue(path)

    /** Get the value as list of node values. Will return an error if the value can not be coerced into a list. */
    open fun asList(): Result<ListNode, NodeError> = incompatibleValue(path)

    abstract fun <T> incompatibleValue(path: Path) : Result<T, NodeError>

    /** path from root configuration to this node. Calculated because this value will change is current
     * root is added to another container. */
    val path: Path
        get() {
        val steps = mutableListOf<Step>()
        populate(steps)
        return Path(steps)
    }

    private fun populate(steps: MutableList<Step>) {
        parent?.populate(steps)
        usage?.let{ steps.add(it) }
    }

    internal fun setParent(parent: Node, usage: Step) {
        require(this.parent == null) { "Node can not be added to parent, as it already had a parent" }
        this.parent = parent
        this.usage = usage
    }
}

fun String.asNode() = LeafNode(this)

fun Int.asNode() = LeafNode(this.toString())

class NullNode : Node() {
    override fun <T> incompatibleValue(path: Path) = Err(IncompatibleValue(path))

    override fun toString() = "_"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode() = javaClass.hashCode()
}

/** A node value that stores a simple value (serialized as a string). */
data class LeafNode(private val data: String) : Node() {

    override fun <T> incompatibleValue(path: Path) = Err(IncompatibleValue(path))

    override fun asString(): Result<String, NodeError> {
        return Ok(data)
    }

    override fun asInt(): Result<Int, NodeError> {
        val result = data.toIntOrNull()
        return if (result != null) {
            Ok(result)
        } else {
            Err(IncompatibleValue(path))
        }
    }

    override fun toString() = data
}

/**
 * A structured node, where values can be addressed by labels.
 */
class StructNode(values: Map<String, Node>) : Node() {

    init { values.forEach { (label, value) -> value.setParent(this, LabelStep(label)) } }

    override fun <T> incompatibleValue(path: Path) = Err(IncompatibleValue(path))

    private val values = values.toMap()

    operator fun get(label: String) : Result<Node, NodeError> {
        val value = values[label]
        return if (value != null) {
            Ok(value)
        } else {
            Err(UndefinedValue(path + LabelStep(label)))
        }
    }

    override fun asStruct() = Ok(this)

    override fun toString(): String {
        return values
                .map {"${it.key}=${it.value}"}
                .joinToString (
                prefix = "{",
                postfix = "}",
                separator = " "
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StructNode

        return values == other.values
    }

    override fun hashCode() = values.hashCode()
}


/**
 * A ordered node, where values can be addressed by index (starting from one).
 */
class ListNode(nodes: List<Node>) : Iterable<Node>, Node() {

    init { nodes.forEachIndexed { index, node -> node.setParent(this, IndexStep(index + 1)) } }

    override fun <T> incompatibleValue(path: Path) = Err(IncompatibleValue(path))

    private val values = nodes.toList()

    operator fun get(index: Int) : Result<Node, NodeError> {
        return when {
            index < 1 -> Err(UndefinedValue(path + IndexStep(index)))
            index > values.size -> Err(UndefinedValue(path + IndexStep(index)))
            else -> Ok(values[index - 1])
        }
    }

    override fun iterator(): Iterator<Node> = values.iterator()

    override fun asList() = Ok(this)

    override fun toString(): String {
        return values.joinToString (
                prefix = "[",
                postfix = "]",
                separator = " "
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ListNode

        return values ==  other.values
    }

    override fun hashCode() = values.hashCode()

}
