package renetik.android.framework.event

import android.content.Context
import android.view.View
import renetik.android.framework.event.CSEvent.CSEventRegistration

@JvmName("eventWithType")
fun <T> event(): CSEvent<T> = CSEventImpl()

fun event(): CSEvent<Unit> = CSEventImpl()

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

interface CSEvent<T> {

    val isListened: Boolean

    fun add(listener: (registration: CSEventRegistration, argument: T) -> Unit): CSEventRegistration

    fun fire(argument: T)

    fun clear()

    interface CSEventRegistration {

        var isActive: Boolean

        fun cancel()
    }
}

fun CSEventRegistration.pause() = apply {
    isActive = false
}

fun CSEventRegistration.resume() = apply {
    isActive = true
}

interface CSEventOwner {
    fun register(registration: CSEventRegistration): CSEventRegistration
}

fun CSEventOwner.register(registration: CSEventRegistration?) =
    registration?.let { register(it) }

interface CSVisibleEventOwner {
    fun whileShowing(registration: CSEventRegistration): CSEventRegistration
}

fun CSVisibleEventOwner.whileShowing(registration: CSEventRegistration?) =
    registration?.let { whileShowing(it) }


interface CSHasParent {
    fun onAddedToParent()
    fun onRemovedFromParent()
}

interface CSViewInterface : CSContextInterface, CSHasDestroy, CSEventOwner {
    val view: View
}

interface CSContextInterface {
    val context: Context
}

interface CSHasDestroy {
    val onDestroy: CSEvent<Unit>
}