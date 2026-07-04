package renetik.android.core.lang.result

import kotlinx.coroutines.CancellableContinuation

fun CancellableContinuation<CSResult<Unit>>.success() =
    resumeWith(Result.success(CSResult.success(Unit)))

fun <Value> CancellableContinuation<CSResult<Value>>.success(value: Value) =
    resumeWith(Result.success(CSResult.success(value)))

fun <Value> CancellableContinuation<CSResult<Value>>.canceled() =
    resumeWith(Result.success(CSResult.cancel()))

fun <Value> CancellableContinuation<CSResult<Value>>.failure(
    throwable: Throwable, message: String?
) = resumeWith(Result.success(CSResult.failure(throwable, message)))

fun <Value> CancellableContinuation<CSResult<Value>>.failure(throwable: Throwable) =
    resumeWith(Result.success(CSResult.failure(throwable)))

fun <Value> CancellableContinuation<CSResult<Value>>.failure(message: String) =
    resumeWith(Result.success(CSResult.failure(message)))