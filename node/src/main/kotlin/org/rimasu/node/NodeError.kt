package org.rimasu.node

/**
 * A possible error when accessing a node.
 */
sealed class NodeError
{
}

/**
 * Error returned when a undefined key is accessed.
 */
data class UndefinedValue(val key: String) : NodeError()


/**
 * Error returned when a value could not be coerced into the requested type.
 */
class IncompatibleValue : NodeError()


/**
 * Error returned when a accessing a value in a list with an index outside the range. The first item
 * in a list is value 1, not value 0.
 */
data class InvalidIndex(val index: Int): NodeError()