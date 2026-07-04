@file:Suppress("NOTHING_TO_INLINE")
@file:OptIn(ExperimentalAtomicApi::class)

package renetik.android.event.change

import kotlinx.coroutines.suspendCancellableCoroutine
import renetik.android.core.kotlin.primitives.isTrue
import renetik.android.event.property.CSSafeHasChangeValue
import renetik.android.event.registration.CSRegistration
import kotlin.Result.Companion.success
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

inline fun <T> CSSafeHasChangeValue<T?>.isNull(): CSSafeHasChangeValue<Boolean> = isSetTo(null)

inline fun <T> CSSafeHasChangeValue<T?>.isNotNull(): CSSafeHasChangeValue<Boolean> = !isSetTo(null)

inline infix fun <T> CSSafeHasChangeValue<T>.isSetTo(value: T): CSSafeHasChangeValue<Boolean> =
    delegate(fromValue = { it == value })

suspend fun <T> CSSafeHasChangeValue<T>.waitFor(condition: (T) -> Boolean) {
    if (!condition(value)) suspendCancellableCoroutine { coroutine ->
        val registration = AtomicReference<CSRegistration?>(null)
        fun resume() = registration.exchange(null)?.apply {
            cancel()
            coroutine.resumeWith(success(Unit))
        }
        registration.store(onUnsafeChange { if (condition(value)) resume() })
        if (condition(value)) resume()
        coroutine.invokeOnCancellation { registration.exchange(null)?.cancel() }
    }
}

@JvmName("CSSafeHasChangeValueBooleanNot")
operator fun CSSafeHasChangeValue<Boolean>.not(): CSSafeHasChangeValue<Boolean> =
    delegate(fromValue = { !it })

inline fun CSSafeHasChangeValue<Boolean>.onUnsafeTrue(
    crossinline function: () -> Unit
): CSRegistration =
    onUnsafeChange { if (it.isTrue) function() }

inline fun <T> CSSafeHasChangeValue<T>.unsafeAction(
    noinline function: (T) -> Unit): CSRegistration {
    onValue(function)
    return onUnsafeChange(function)
}

