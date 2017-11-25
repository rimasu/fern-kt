package com.github.rimasu.node.types

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Err
import kotlin.test.fail


fun <T, E> assertErr(result: Result<T, E>, check: (E) -> Unit)  {
    when(result) {
        is Ok -> fail("Result ok, expected failure $result")
        is Err -> check(result.error)
    }
}

fun <T, E> assertOk(result: Result<T, E>, check: (T) -> Unit)  {
    when(result) {
        is Ok -> check(result.value)
        is Err -> fail("Result err, expected ok $result")
    }
}