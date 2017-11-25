package org.rimasu.node

/**
 * A possible error when accessing a node.
 */
sealed class NodeError
{
    abstract val path: Path
}

/**
 * Error returned when a undefined label is accessed.
 */
data class UndefinedValue(override val path: Path) : NodeError()


/**
 * Error returned when a value could not be coerced into the requested type.
 */
class IncompatibleValue(override val path: Path) : NodeError()
