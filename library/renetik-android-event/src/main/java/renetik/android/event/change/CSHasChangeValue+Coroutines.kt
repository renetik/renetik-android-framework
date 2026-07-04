@file:OptIn(ExperimentalAtomicApi::class)

package renetik.android.event.change

import kotlinx.coroutines.suspendCancellableCoroutine
import renetik.android.event.registration.CSRegistration
import kotlin.Result.Companion.success
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

suspend inline fun <reified T> CSHasChangeValue<*>.onType(): T =
    value as? T ?: suspendCancellableCoroutine {
        value as? T ?: run {
            var registration: CSRegistration? = null
            registration = onChange { value ->
                (value as? T)?.also { type ->
                    registration?.cancel()
                    success(type)
                }
            }
            it.invokeOnCancellation { registration.cancel() }
        }
    }

suspend fun <T> CSHasChangeValue<T>.waitFor(condition: (T) -> Boolean) {
    if (!condition(value)) suspendCancellableCoroutine { coroutine ->
        val registration = AtomicReference<CSRegistration?>(null)
        fun resume() = registration.exchange(null)?.apply {
            cancel()
            coroutine.resumeWith(success(Unit))
        }
        registration.store(onChange { if (condition(value)) resume() })
        if (condition(value)) resume()
        coroutine.invokeOnCancellation { registration.exchange(null)?.cancel() }
    }
}