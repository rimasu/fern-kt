package org.rimasu.fuse.config

import com.danneu.result.Result

/** A value of a field in a configuration scope. */
sealed class ConfigValue {

    internal lateinit var path: List<String>

    /** Get the value as a string. Will return an error if the value can not be coerced into a string. */
    abstract fun asString(): Result<String, ConfigError>

    /** Get the value as an integer. Will return an error if the value can not be coerced into a int. */
    abstract fun asInt(): Result<Int, ConfigError>

    /** Get the value as config. Will return an error if the value can not be coerced into a configuration. */
    abstract fun asConfig(): Result<MutableConfig, ConfigError>

    /** Get the value as list of config values. Will return an error if the value can not be coerced into a list. */
    abstract fun asList(): Result<Collection<ConfigValue>, ConfigError>
}

/** A configuration value that stores a simple value (serialized as a string). */
class DataConfigValue(private val data: String) : ConfigValue() {
    override fun asString(): Result<String, ConfigError> {
        return Result.ok(data)
    }

    override fun asInt(): Result<Int, ConfigError> {
        val result = data.toIntOrNull()
        return if (result != null) {
            Result.ok(result)
        } else {
            Result.err(ConfigError(path))
        }
    }

    override fun asConfig(): Result<MutableConfig, ConfigError> {
        return Result.err(ConfigError(path))
    }

    override fun asList(): Result<List<ConfigValue>, ConfigError> {
        return Result.err(ConfigError(path))
    }
}

/** A configuration value that contains another configuration value. */
class NestedConfigValue(private val data: MutableConfig) : ConfigValue() {

    override fun asString(): Result<String, ConfigError> {
        return Result.err(ConfigError(path))
    }

    override fun asInt(): Result<Int, ConfigError> {
        return Result.err(ConfigError(path))
    }

    override fun asConfig(): Result<MutableConfig, ConfigError> {
        return Result.ok(data)
    }

    override fun asList(): Result<List<ConfigValue>, ConfigError> {
        return Result.err(ConfigError(path))
    }
}

/** A configuration value that contains a list of configuration values. */
class ListConfigValue(private val data: List<ConfigValue>) : ConfigValue() {

    override fun asString(): Result<String, ConfigError> {
        return Result.err(ConfigError(path))
    }

    override fun asInt(): Result<Int, ConfigError> {
        return Result.err(ConfigError(path))
    }

    override fun asConfig(): Result<MutableConfig, ConfigError> {
        return Result.err(ConfigError(path))
    }

    override fun asList(): Result<List<ConfigValue>, ConfigError> {
        return Result.ok(data)
    }
}