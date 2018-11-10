package renetik.android.extensions

import renetik.android.java.event.CSEvent
import renetik.android.java.event.CSEvent.CSEventRegistration

fun <T> CSEvent<T>.execute(function: (argument: T) -> Unit): CSEventRegistration {
    return this.add { _, arg -> function(arg) }
}

fun <T> CSEvent<T>.run(function: (registration: CSEventRegistration, argument: T) -> Unit)
        : CSEventRegistration {
    return this.add { reg, arg -> function(reg, arg) }
}