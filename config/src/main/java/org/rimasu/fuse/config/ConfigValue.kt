package org.rimasu.fuse.config

import com.danneu.result.Result

/**
 * A value of a field in a configuration scope.
 */
sealed class ConfigValue {

    abstract fun asString(): Result<String, ConfigError>
}

class DataConfigValue(private val data: String) : ConfigValue() {

    override fun asString(): Result<String, ConfigError> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}