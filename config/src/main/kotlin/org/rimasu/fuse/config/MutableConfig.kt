package org.rimasu.fuse.config

import com.danneu.result.Result

/**
 * A configuration scope that be mutated.
 */
class MutableConfig {

    private val values = mutableMapOf<String, ConfigValue>()

    operator fun get(key: String) : Result<ConfigValue, ConfigError> {
        val value = values[key]
        return if (value != null) {
            Result.ok(value)
        } else {
            Result.err(ConfigError(listOf(key)))
        }
    }

    operator fun set(key: String, value: String) {
        set(key, DataConfigValue(value))
    }

    private fun set(key: String, value: ConfigValue) {
        values[key] = value
    }
}

