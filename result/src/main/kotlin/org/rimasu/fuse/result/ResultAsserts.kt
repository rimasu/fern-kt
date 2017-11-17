package org.rimasu.fuse.result

import com.danneu.result.Result
import kotlin.test.fail


fun <T, E> assertErr(result: Result<T, E>, check: (E) -> Unit)  {
    when(result) {
        is Result.Ok -> fail("Result ok, expected failure $result")
        is Result.Err -> check(result.error)
    }
}

fun <T, E> assertOk(result: Result<T, E>, check: (T) -> Unit)  {
    when(result) {
        is Result.Ok -> check(result.value)
        is Result.Err -> fail("Result err, expected ok $result")
    }
}