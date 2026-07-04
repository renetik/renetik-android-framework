package renetik.android.event.change

import renetik.android.core.kotlin.className
import renetik.android.core.lang.Fun
import renetik.android.event.dispatch.CSDebouncer.Companion.debouncer
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistrationsMap

inline fun <T : CSHasChange<*>> Array<T>.onChangeLater(
    crossinline function: Fun
): CSRegistration {
    val registrations = CSRegistrationsMap(className)
    val laterOnceFunction = registrations.debouncer { function() }
    forEach { registrations.register(it.onChange { laterOnceFunction() }) }
    return registrations
}

inline fun <T : CSHasChange<*>> Array<T>.onChange(
    crossinline function: Fun
): CSRegistration {
    val registrations = CSRegistrationsMap(className)
    forEach { registrations.register(it.onChange(function)) }
    return registrations
}

inline fun <T : CSHasChange<*>> Array<T>.actionOnChangeLater(
    crossinline function: Fun
): CSRegistration {
    function()
    return onChangeLater(function)
}