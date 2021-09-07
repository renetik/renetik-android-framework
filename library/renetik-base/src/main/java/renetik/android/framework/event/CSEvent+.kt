package renetik.android.framework.event

fun CSEvent<Unit>.fire() = apply { fire(Unit) }

inline fun CSEvent<Unit>.listen(crossinline function: () -> Unit) =
    this.add { _, _ -> function() }

inline fun <T> CSEvent<T>.listen(crossinline function: (argument: T) -> Unit) =
    this.add { _, argument -> function(argument) }

inline fun <T> CSEvent<T>.listenOnce(crossinline listener: (argument: T) -> Unit) =
    add { registration, argument ->
        registration.cancel()
        listener(argument)
    }