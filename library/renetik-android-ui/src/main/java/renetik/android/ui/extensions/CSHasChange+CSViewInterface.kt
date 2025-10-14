package renetik.android.ui.extensions

import renetik.android.core.kotlin.className
import renetik.android.core.lang.Fun
import renetik.android.event.common.CSDebouncer.Companion.debouncer
import renetik.android.event.registration.CSHasChange
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistrationsMap
import renetik.android.ui.protocol.CSViewInterface


inline fun <Argument> CSHasChange<Argument>.onChangeAfterLayout(
    parent: CSViewInterface,
    crossinline function: Fun
): CSRegistration {
    val registrations = CSRegistrationsMap(className)
    val laterOnceFunction = registrations.debouncer { function() }
    registrations.register(onChange {
        registrations.register(parent.registerAfterLayout {
            laterOnceFunction()
        })
    })
    return registrations
}