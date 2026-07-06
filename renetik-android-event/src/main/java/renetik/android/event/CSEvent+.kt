package renetik.android.event

import renetik.android.core.lang.Fun
import renetik.android.event.change.CSHasChangeValue
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.register

fun CSEvent<Unit>.fire() = apply { fire(Unit) }

operator fun CSEvent<Unit>.invoke() {
    fire(Unit)
}

operator fun <T> CSEvent<T>.invoke(param: T) = apply { fire(param) }

@JvmName("listenNoArg")
fun CSEvent<*>.listen(function: Fun): CSRegistration = listen { _, _ -> function() }

inline fun CSEvent<Unit>.listen(crossinline function: Fun) = listen { _, _ -> function() }

inline fun CSEvent<*>.action(crossinline function: () -> Unit): CSRegistration {
    function()
    return this.listen { _, _ -> function() }
}

inline fun CSEvent<*>.action(crossinline function: (CSRegistration) -> Unit): CSRegistration {
    val registration = this.listen { registration, _ -> function(registration) }
    function(registration)
    return registration
}

inline fun <T> CSEvent<T>.listen(
    crossinline function: (registration: CSRegistration, argument: T) -> Unit
): CSRegistration {
    lateinit var registration: CSRegistration
    registration = listen { function(registration, it) }
    return registration
}

inline fun <T> CSEvent<T>.listenOnce(
    crossinline listener: (argument: T) -> Unit
): CSRegistration =
    listen { registration, argument ->
        registration.cancel()
        listener(argument)
    }

fun CSEvent<*>.paused(function: Fun) {
    pause()
    function()
    resume()
}

fun <EventArg, PropertyType> CSEvent<EventArg>.delegate(
    parent: CSHasRegistrations? = null, from: () -> PropertyType
): CSHasChangeValue<PropertyType> {
    val property = property(from())
    listen { property.value = from() }.also { parent?.register(it) }
    return property
}

fun <EventArg, PropertyType> CSEvent<EventArg>.delegateLate(
    parent: CSHasRegistrations? = null, from: () -> PropertyType
): CSHasChangeValue<PropertyType?> {
    val property = property<PropertyType?>(null)
    listen { property.value = from() }.also { parent?.register(it) }
    return property
}