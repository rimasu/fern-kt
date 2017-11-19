package org.rimasu.config

/**
 * A possible error when access a configuration scope.
 */
data class ConfigError(
        /**
         * List of all the keys from the root of the configuration to the key associated with the error.
         */
        val keyPath: List<String>
) {

}