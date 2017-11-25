package com.github.rimasu.node.types

/**
 * A step in a path between a containing node and a contained node.
 */
sealed class Step

/**
 * A step in a path that is identified using a label.
 */
data class LabelStep(val label: String) : Step()
{
    override fun toString() = "/$label"
}

/**
 * A step in a path that is identified using an index (indexes start from one).
 */
data class IndexStep(val index: Int): Step()
{
    override fun toString() = "#$index"
}