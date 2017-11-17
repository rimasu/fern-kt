import com.danneu.result.Result
import kotlin.test.fail


fun <T, E> assertErr(value: Result<T, E>, check: (E) -> Unit)  {
    when(value) {
        is Result.Ok -> fail("Result ok, expected failure $value")
        is Result.Err -> check(value.error)
    }
}

fun <T, E> assertOk(value: Result<T, E>, check: (T) -> Unit)  {
    when(value) {
        is Result.Ok -> check(value.value)
        is Result.Err -> fail("Result err, expected ok $value")
    }
}