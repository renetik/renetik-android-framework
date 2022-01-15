package renetik.android.framework.event

import androidx.annotation.UiThread

fun CSEvent<Unit>.fire() = apply { fire(Unit) }

inline fun CSEvent<Unit>.listen(@UiThread crossinline function: () -> Unit) =
    this.add { _, _ -> function() }

inline fun <T> CSEvent<T>.listen(@UiThread crossinline function: (argument: T) -> Unit) =
    this.add { _, argument -> function(argument) }

inline fun <T> CSEvent<T>.listenOnce(@UiThread crossinline listener: (argument: T) -> Unit) =
    add { registration, argument ->
        registration.cancel()
        listener(argument)
    }