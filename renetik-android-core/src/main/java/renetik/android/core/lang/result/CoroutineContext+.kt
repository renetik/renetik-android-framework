package renetik.android.core.lang.result

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

fun CoroutineContext.named(name: String): CoroutineContext =
    this + CoroutineName(name)

suspend inline operator fun <T> CoroutineContext.invoke(
    crossinline block: suspend () -> T
): T = withContext(this) { block() }

@JvmName("CoroutineContextNullInvoke")
suspend inline operator fun <T> CoroutineContext?.invoke(
    crossinline block: suspend () -> T
): T = this?.let { withContext(this) { block() } } ?: block()