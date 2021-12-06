package renetik.android.framework.event.property

import renetik.android.framework.event.CSEventRegistration
import renetik.android.primitives.isFalse
import renetik.android.primitives.isTrue

fun CSEventProperty<Boolean>.onFalse(function: () -> Unit) =
    onChange { if (it.isFalse) function() }

fun CSEventProperty<Boolean>.onTrue(function: () -> Unit) =
    onChange { if (it.isTrue) function() }

fun <T> CSEventProperty<T>.listenChangeOnce(listener: (argument: T) -> Unit): CSEventRegistration {
    lateinit var registration: CSEventRegistration
    registration = onChange { argument: T ->
        registration.cancel()
        listener(argument)
    }
    return registration
}

fun CSEventProperty<Boolean>.listenUntilTrueOnce(listener: (argument: Boolean) -> Unit): CSEventRegistration {
    lateinit var registration: CSEventRegistration
    registration = onChange { argument: Boolean ->
        if (argument) {
            registration.cancel()
            listener(argument)
        }
    }
    return registration
}

fun CSEventProperty<Boolean>.listenUntilFalseOnce(listener: (argument: Boolean) -> Unit): CSEventRegistration {
    lateinit var registration: CSEventRegistration
    registration = onChange { argument: Boolean ->
        if (!argument) {
            registration.cancel()
            listener(argument)
        }
    }
    return registration
}