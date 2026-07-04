@file:Suppress("NOTHING_TO_INLINE")

package renetik.android.event

import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.event.property.CSSafeHasChange
import renetik.android.event.registration.CSRegistration

interface CSSafeEvent<T> : CSSafeHasChange<T>, CSEvent<T> {
    companion object {
        @JvmName("eventWithArgument")
        inline fun <T> CSHasDestruct.safeEvent(): CSSafeEvent<T> = CSSafeEventImpl(this)

        inline fun CSHasDestruct.safeEvent(): CSSafeEvent<Unit> = CSSafeEventImpl(this)

        val Empty: CSSafeEvent<Unit> = safeEmpty()
        inline fun <T> safeEmpty(): CSSafeEvent<T> = object : CSSafeEvent<T> {
            override fun pause() = Unit
            override fun resume() = Unit
            override val isListened: Boolean = false
            override fun clear() = Unit
            override fun onChange(function: (T) -> Unit) = CSRegistration.Empty
            override fun fire(argument: T) = Unit
            override fun listen(function: (argument: T) -> Unit) = CSRegistration.Empty
            override fun onUnsafeChange(function: (T) -> Unit) = CSRegistration.Empty
        }
    }
}
