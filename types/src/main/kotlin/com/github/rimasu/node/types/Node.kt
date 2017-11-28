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
import com.github.rimasu.text.Region

/** A piece of node */
sealed class Node {

    private var parent: Node? = null
    private var usage: Step? = null

    /** An optional anchor than describes the text region in a source document that contained the serialized node. */
    abstract val anchor: Region?

    /** Get the value as a string. Will return an error if the value can not be coerced into a string. */
    open fun asString(): Result<String, NodeError> = Err(IncompatibleValue(path, anchor))

    /** Get the value as an integer. Will return an error if the value can not be coerced into a int. */
    open fun asInt(): Result<Int, NodeError> =  Err(IncompatibleValue(path, anchor))

    /** Get the value as a long. Will return an error if the value can not be coerced into a long. */
    open fun asLong(): Result<Long, NodeError> =  Err(IncompatibleValue(path, anchor))

    /** Get the value as a float. Will return an error if the value can not be coerced into a float. */
    open fun asFloat(): Result<Float, NodeError> = Err(IncompatibleValue(path, anchor))

    /** Get the value as a double. Will return an error if the value can not be coerced into a double. */
    open fun asDouble(): Result<Double, NodeError> = Err(IncompatibleValue(path, anchor))

    /** Get the value as a boolean. Will return an error if the value can not be coerced into a boolean. */
    open fun asBoolean(): Result<Boolean, NodeError> = Err(IncompatibleValue(path, anchor))

    /** Get the value as node. Will return an error if the value can not be coerced into a node. */
    open fun asStruct(): Result<StructNode, NodeError> = Err(IncompatibleValue(path, anchor))

    /** Get the value as list of node values. Will return an error if the value can not be coerced into a list. */
    open fun asList(): Result<ListNode, NodeError> = Err(IncompatibleValue(path, anchor))

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

fun String.asNode(anchor: Region? = null) = LeafNode(this, anchor)

fun Int.asNode(anchor: Region? = null) = LeafNode(this.toString(), anchor)

fun Long.asNode(anchor: Region? = null) = LeafNode(this.toString(), anchor)

fun Double.asNode(anchor: Region? = null) = LeafNode(this.toString(), anchor)

fun Float.asNode(anchor: Region? = null) = LeafNode(this.toString(), anchor)

fun Boolean.asNode(anchor: Region? = null) = LeafNode(this.toString(), anchor)

class NullNode(override val anchor: Region? = null) : Node() {

    override fun toString() = "_"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode() = javaClass.hashCode()
}

/** A node value that stores a simple value (serialized as a string). */
data class LeafNode(private val data: String, override val anchor: Region? = null) : Node() {

    override fun asString(): Result<String, NodeError> {
        return Ok(data)
    }

    override fun asInt(): Result<Int, NodeError> {
        val result = data.toIntOrNull()
        return if (result != null) {
            Ok(result)
        } else {
            Err(IncompatibleValue(path, anchor))
        }
    }

    override fun asLong(): Result<Long, NodeError> {
        val result = data.toLongOrNull()
        return if (result != null) {
            Ok(result)
        } else {
            Err(IncompatibleValue(path, anchor))
        }
    }

    override fun asFloat(): Result<Float, NodeError> {
        val result = data.toFloatOrNull()
        return if (result != null) {
            Ok(result)
        } else {
            Err(IncompatibleValue(path, anchor))
        }
    }

    override fun asDouble(): Result<Double, NodeError> {
        val result = data.toDoubleOrNull()
        return if (result != null) {
            Ok(result)
        } else {
            Err(IncompatibleValue(path, anchor))
        }
    }

    override fun asBoolean(): Result<Boolean, NodeError> {
        return when(data.toLowerCase()) {
            "true" -> Ok(true)
            "false"-> Ok(false)
            else ->  Err(IncompatibleValue(path, anchor))

        }
    }

    override fun toString() = data
}

/**
 * A structured node, where values can be addressed by labels.
 */
class StructNode(values: Map<String, Node>, override val anchor: Region? = null) : Node() {

    init { values.forEach { (label, value) -> value.setParent(this, LabelStep(label)) } }

    private val values = values.toMap()

    operator fun get(label: String) : Result<Node, NodeError> {
        val value = values[label]
        return if (value != null) {
            Ok(value)
        } else {
            Err(UndefinedValue(path + LabelStep(label), anchor))
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
class ListNode(nodes: List<Node>, override val anchor: Region? = null) : Iterable<Node>, Node() {

    init { nodes.forEachIndexed { index, node -> node.setParent(this, IndexStep(index + 1)) } }

    private val values = nodes.toList()

    operator fun get(index: Int) : Result<Node, NodeError> {
        return when {
            index < 1 -> Err(UndefinedValue(path + IndexStep(index), anchor))
            index > values.size -> Err(UndefinedValue(path + IndexStep(index), anchor))
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
