package renetik.android.event.change

import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.plus

inline fun <T> CSHasRegistrations.onChangeOnce(
    event: CSHasChange<T>,
    crossinline listener: (argument: T) -> Unit
) = this + event.onChange { registration, argument ->
    if (registration.isActive) listener(argument)
}

inline fun CSHasRegistrations.onChangeOnce(
    event: CSHasChange<*>,
    crossinline listener: () -> Unit
) = onChangeOnce(event) { _ -> listener() }

inline fun CSHasRegistrations.listenUntilTrueOnce(
    hasChange: CSHasChange<Boolean>,
    crossinline listener: () -> Unit
) = this + hasChange.onChange { registration, value ->
    if (registration.isActive && value) listener()
}