package org.rimasu.config

/**
 * A possible error when access a configuration scope.
 */
sealed class ConfigError
{
}

/**
 * Error returned when a undefined key is accessed.
 */
data class UndefinedValue(val key: String) : ConfigError()


/**
 * Error returned when a value could not be coerced into the requested type.
 */
class IncompatibleValue : ConfigError()


/**
 * Error returned when a accessing a value in a list with an index outside the range. The first item
 * in a list is value 1, not value 0.
 */
data class InvalidIndex(val index: Int): ConfigError()