package org.rimasu.config

import com.danneu.result.Result
import com.winterbe.expekt.should
import org.rimasu.fuse.result.assertErr
import org.rimasu.fuse.result.assertOk

abstract class WhenGettingConfig<T>(protected val config: Config) {
    protected abstract val result: Result<T, ConfigError>

    protected fun assertIncompatibleValue() {
        assertErr(result) {
            it.should.instanceof(IncompatibleValue::class.java)
        }
    }

    protected fun assertValueRetrieved(expected: T) {
        assertOk(result) {
            it.should.equal(expected)
        }
    }
}

abstract class WhenGettingConfigAsString(config:  Config) : WhenGettingConfig<String>(config) {
    override val result = config.asString()
}

abstract class WhenGettingConfigAsInt(config: Config) : WhenGettingConfig<Int>(config) {
    override val result = config.asInt()
}

abstract class WhenGettingConfigAsStruct(config: Config) : WhenGettingConfig<ConfigStruct>(config) {
    override val result = config.asStruct()
}

abstract class WhenGettingConfigAsList(config: Config): WhenGettingConfig<ConfigList>(config)  {
    override val result = config.asList()
}