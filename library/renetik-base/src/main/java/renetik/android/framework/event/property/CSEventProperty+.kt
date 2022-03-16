package renetik.android.framework.event.property

import renetik.android.framework.event.CSRegistration
import renetik.android.framework.lang.isFalse
import renetik.android.framework.lang.isTrue
import renetik.android.primitives.isFalse
import renetik.android.primitives.isTrue

fun <T> CSEventProperty<T?>.clear() = value(null)
val <T> CSEventProperty<T?>.isEmpty get() = value == null
val <T> CSEventProperty<T?>.isSet get() = !isEmpty

fun <T> CSEventProperty<T>.action(function: (T) -> Unit): CSRegistration {
    function(value)
    return onChange(function)
}

fun CSEventProperty<Boolean>.onFalse(function: () -> Unit) =
    onChange { if (it.isFalse) function() }

fun CSEventProperty<Boolean>.onTrue(function: () -> Unit) =
    onChange { if (it.isTrue) function() }

fun CSEventProperty<Boolean>.actionIf(function: () -> Unit): CSRegistration {
    if (isTrue) function()
    return onTrue(function)
}

fun CSEventProperty<Boolean>.actionIfNot(function: () -> Unit): CSRegistration {
    if (isFalse) function()
    return onFalse(function)
}

fun <T> CSEventProperty<T>.listenChangeOnce(listener: (argument: T) -> Unit): CSRegistration {
    lateinit var registration: CSRegistration
    registration = onChange { argument: T ->
        registration.cancel()
        listener(argument)
    }
    return registration
}

fun CSEventProperty<Boolean>.listenUntilTrueOnce(listener: (argument: Boolean) -> Unit): CSRegistration {
    lateinit var registration: CSRegistration
    registration = onChange { argument: Boolean ->
        if (argument) {
            registration.cancel()
            listener(argument)
        }
    }
    return registration
}

fun CSEventProperty<Boolean>.listenUntilFalseOnce(listener: (argument: Boolean) -> Unit): CSRegistration {
    lateinit var registration: CSRegistration
    registration = onChange { argument: Boolean ->
        if (!argument) {
            registration.cancel()
            listener(argument)
        }
    }
    return registration
}

fun <T : CSEventProperty<Int>> T.keepMax(maxValue: Int, fire: Boolean = true) = apply {
    if (value > maxValue) value(maxValue, fire)
    onChange { if (value > maxValue) value(maxValue, fire) }
}