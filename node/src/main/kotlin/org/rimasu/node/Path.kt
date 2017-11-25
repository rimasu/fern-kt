package org.rimasu.node

/**
 * A path from a node (normally the root) to another node.
 */
data class Path(val steps: List<Step>) {

    override fun toString() = steps.joinToString (
            prefix = "{",
            postfix = "}",
            separator = ""
    )

    operator fun plus(next: Step) = Path(steps + next)
}