package org.rimasu.node

import com.danneu.result.Result

/** A piece of node */
sealed class Node {

    private var parent: Node? = null
    private var usage: Step? = null

    /** Get the value as a string. Will return an error if the value can not be coerced into a string. */
    open fun asString(): Result<String, NodeError> = incompatibleValue()

    /** Get the value as an integer. Will return an error if the value can not be coerced into a int. */
    open fun asInt(): Result<Int, NodeError> =  incompatibleValue()

    /** Get the value as node. Will return an error if the value can not be coerced into a node. */
    open fun asStruct(): Result<StructNode, NodeError> = incompatibleValue()

    /** Get the value as list of node values. Will return an error if the value can not be coerced into a list. */
    open fun asList(): Result<ListNode, NodeError> = incompatibleValue()

    abstract fun <T> incompatibleValue() : Result<T, NodeError>

    /** path from root configuration to this node. Calculated because this value will change is current
     * root is added to another container. */
    val path: Path get() {
        val steps = mutableListOf<Step>()
        populate(steps)
        return Path(steps)
    }

    private fun populate(steps: MutableList<Step>) {
        parent?.populate(steps)
        usage?.let{ steps.add(it) }
    }

    internal fun setParent(parent: Node, usage: Step) {
        require(this.parent == null)
        this.parent = parent
        this.usage = usage
    }
}

fun String.asNode() = LeafNode(this)

fun Int.asNode() = LeafNode(this.toString())

class NullNode : Node() {
    override fun <T> incompatibleValue() = Result.err(IncompatibleValue())
}

/** A node value that stores a simple value (serialized as a string). */
class LeafNode(private val data: String) : Node() {

    override fun <T> incompatibleValue() = Result.err(IncompatibleValue())

    override fun asString(): Result<String, NodeError> {
        return Result.ok(data)
    }

    override fun asInt(): Result<Int, NodeError> {
        val result = data.toIntOrNull()
        return if (result != null) {
            Result.ok(result)
        } else {
            Result.err(IncompatibleValue())
        }
    }

    override fun toString() = data
}

/**
 * A structured node, where values can be addressed by labellabels.
 */
class StructNode(values: Map<String, Node>) : Node() {

    init { values.forEach { (label, value) -> value.setParent(this, LabelStep(label)) } }

    override fun <T> incompatibleValue() = Result.err(IncompatibleValue())

    private val values = values.toMap()

    operator fun get(key: String) : Result<Node, NodeError> {
        val value = values[key]
        return if (value != null) {
            Result.ok(value)
        } else {
            Result.err(UndefinedValue(key))
        }
    }

    override fun asStruct() = Result.ok(this)
}


/**
 * A ordered node, where values can be addressed by index (starting from one).
 */
class ListNode(nodes: List<Node>) : Iterable<Node>, Node() {

    init { nodes.forEachIndexed { index, node -> node.setParent(this, IndexStep(index + 1)) } }

    override fun <T> incompatibleValue() = Result.err(IncompatibleValue())

    private val values = nodes.toList()

    operator fun get(index: Int) : Result<Node, NodeError> {
        return when {
            index < 1 -> Result.err(InvalidIndex(index))
            index > values.size -> Result.err(InvalidIndex(index))
            else -> Result.ok(values[index - 1])
        }
    }

    override fun iterator(): Iterator<Node> = values.iterator()

    override fun asList() = Result.ok(this)
}
