package org.rimasu.config

import com.danneu.result.Result

/** A piece of config */
sealed class Config {

    /** Get the value as a string. Will return an error if the value can not be coerced into a string. */
    open fun asString(): Result<String, ConfigError> = incompatibleValue()

    /** Get the value as an integer. Will return an error if the value can not be coerced into a int. */
    open fun asInt(): Result<Int, ConfigError> =  incompatibleValue()

    /** Get the value as config. Will return an error if the value can not be coerced into a configuration. */
    open fun asStruct(): Result<ConfigStruct, ConfigError> = incompatibleValue()

    /** Get the value as list of config values. Will return an error if the value can not be coerced into a list. */
    open fun asList(): Result<ConfigList, ConfigError> = incompatibleValue()

    abstract fun <T> incompatibleValue() : Result<T, ConfigError>
}

fun String.asConfigValue() = ConfigValue(this)

fun Int.asConfigValue() = ConfigValue(this.toString())

class NullConfig : Config() {
    override fun <T> incompatibleValue() = Result.err(IncompatibleValue())
}

/** A configuration value that stores a simple value (serialized as a string). */
class ConfigValue(private val data: String) : Config() {

    override fun <T> incompatibleValue() = Result.err(IncompatibleValue())

    override fun asString(): Result<String, ConfigError> {
        return Result.ok(data)
    }

    override fun asInt(): Result<Int, ConfigError> {
        val result = data.toIntOrNull()
        return if (result != null) {
            Result.ok(result)
        } else {
            Result.err(IncompatibleValue())
        }
    }
}

/**
 * A structured configuration, where values can be addressed by field name.
 */
class ConfigStruct : Config() {

    override fun <T> incompatibleValue() = Result.err(IncompatibleValue())

    private val values = mutableMapOf<String, Config>()

    operator fun get(key: String) : Result<Config, ConfigError> {
        val value = values[key]
        return if (value != null) {
            Result.ok(value)
        } else {
            Result.err(UndefinedValue())
        }
    }

    operator fun set(key: String, value: Config) {
        values[key] = value
    }

    override fun asStruct() = Result.ok(this)
}


/**
 * A ordered configuration, where values can be addressed by index (starting from one).
 */
class ConfigList : Iterable<Config>, Config() {

    override fun <T> incompatibleValue() = Result.err(IncompatibleValue())

    private val values = mutableListOf<Config>()

    operator fun get(index: Int) : Result<Config, ConfigError> {
        return when {
            index < 1 -> Result.err(UndefinedValue())
            index > values.size -> Result.err(UndefinedValue())
            else -> Result.ok(values[index - 1])
        }
    }

    override fun iterator(): Iterator<Config> = values.iterator()

    operator  fun set(key: Int, value: Config) {
        while(key < values.size) {
            values.add(NullConfig())
        }
        values[key - 1] = value
    }

    operator fun plusAssign(value: Config) {
        values.add(value)
    }

    override fun asList() = Result.ok(this)
}
