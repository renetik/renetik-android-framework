package renetik.android.java.event

import renetik.android.java.event.CSEvent.CSEventRegistration

fun <T> event(): CSEvent<T> {
    return CSEventImpl()
}

fun CSEvent<Unit>.fire() = fire(Unit)

fun <T> CSEvent<T>.register(listener: (argument: T) -> Unit): CSEventRegistration {
    return this.add { _, argument -> listener(argument) }
}

fun CSEvent<Unit>.listen(listener: () -> Unit): CSEventRegistration {
    return this.add { _, _ -> listener() }
}

fun <T> CSEvent<T>.listen(listener: (T) -> Unit): CSEventRegistration {
    return this.add { _, argument -> listener(argument) }
}

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



