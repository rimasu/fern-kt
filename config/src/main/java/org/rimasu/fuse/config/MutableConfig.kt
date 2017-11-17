package org.rimasu.fuse.config

import com.danneu.result.Result

/**
 * A configuration scope that be mutated.
 */
class MutableConfig {

    operator fun get(key: String) : Result<ConfigValue, ConfigError> {
        return Result.err(
                ConfigError(
                        keyPath = listOf(key)
                )
        )
    }
}

