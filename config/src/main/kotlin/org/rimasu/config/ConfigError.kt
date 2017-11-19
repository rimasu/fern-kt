package org.rimasu.config

/**
 * A possible error when access a configuration scope.
 */
sealed class ConfigError
{
    /**
     * List of all the keys from the root of the configuration to the key associated with the error.
     */
    abstract val keyPath: List<String>
}

/**
 * Error returned when a undefined key is accessed.
 */
data class UndefinedValue(override val keyPath: List<String>) : ConfigError()


/**
 * Error returned when a value could not be coerced into the requested type.
 */
data class IncompatibleValue(override val keyPath: List<String>) : ConfigError()