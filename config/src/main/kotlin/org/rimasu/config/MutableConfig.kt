package org.rimasu.config

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
            Result.err(UndefinedValue())
        }
    }

    operator fun set(key: String, value: String) {
        set(key, DataConfigValue(value))
    }

    operator fun set(key: String, value: Int) {
        set(key, DataConfigValue(value.toString()))
    }

    operator fun set(key: String, value: MutableConfig) {
        set(key, NestedConfigValue(value))
    }

    operator fun set(key: String, value: Iterable<ConfigValue>) {
        set(key, ListConfigValue(value.toList()))
    }

    private fun set(key: String, value: ConfigValue) {
        values[key] = value
    }
}

