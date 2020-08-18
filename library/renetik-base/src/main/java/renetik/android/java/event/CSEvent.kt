package renetik.android.java.event

import renetik.android.java.event.CSEvent.CSEventRegistration

fun <T> event(): CSEvent<T> = CSEventImpl()

fun CSEvent<Unit>.fire() = fire(Unit)

fun CSEvent<Unit>.listener(function: () -> Unit) =
    this.add { _, _ -> function() }

fun <T> CSEvent<T>.register(listener: (argument: T) -> Unit) =
    this.add { _, argument -> listener(argument) }

fun <T> CSEvent<T>.listener(function: (argument: T) -> Unit) =
    this.add { _, argument -> function(argument) }

interface CSEvent<T> {

    fun add(listener: (registration: CSEventRegistration, argument: T) -> Unit): CSEventRegistration

    fun fire(argument: T)

    fun clear()

    interface CSEventRegistration {

        var isActive: Boolean

        fun cancel()

        fun event(): CSEvent<*>
    }
}

fun <T> CSEvent<T>.runOnce(listener: (registration: CSEventRegistration, argument: T) -> Unit): CSEventRegistration {
    return add { registration, argument ->
        registration.cancel()
        listener(registration, argument)
    }
}



