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
class UndefinedValue : ConfigError()


/**
 * Error returned when a value could not be coerced into the requested type.
 */
class IncompatibleValue : ConfigError()