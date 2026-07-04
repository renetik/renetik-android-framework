package renetik.android.preset.extensions

import renetik.android.core.lang.Fun
import renetik.android.event.change.CSHasChange
import renetik.android.event.change.CSHasChangeValue
import renetik.android.event.change.action
import renetik.android.event.registration.CSRegistration
import renetik.android.preset.Preset

inline fun CSHasChange<*>.onChange(
    preset: Preset, crossinline function: Fun
): CSRegistration {
    val registration = onChange { function() }
    return CSRegistration(
        registration,
        preset.onBeforeChange { registration.pause() },
        preset.onChange {
            function()
            registration.resume()
        }
    )
}

inline fun <T, V : CSHasChangeValue<T>> V.action(
    preset: Preset, crossinline function: (T) -> Unit
): CSRegistration {
    val registration = action { value -> function(value) }
    return CSRegistration(
        registration,
        preset.onBeforeChange { registration.pause() },
        preset.onChange {
            function(value)
            registration.resume()
        }
    )
}