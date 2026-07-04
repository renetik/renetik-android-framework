@file:Suppress("NOTHING_TO_INLINE")

package renetik.android.event

import androidx.annotation.AnyThread
import renetik.android.event.change.CSHasChange
import renetik.android.event.registration.CSRegistration

interface CSEvent<T> : CSHasChange<T> {

    companion object {
        @JvmName("eventWithArgument") inline fun <T> event() = CSEventImpl<T>()

        inline fun event() = CSEventImpl<Unit>()

        val Empty: CSEvent<Unit> = empty()
        inline fun <T> empty(): CSEvent<T> = object : CSEvent<T> {
            override fun pause() = Unit
            override fun resume() = Unit
            override val isListened: Boolean = false
            override fun clear() = Unit
            override fun onChange(function: (T) -> Unit) = CSRegistration.Empty
            override fun fire(argument: T) = Unit
            override fun listen(function: (argument: T) -> Unit) = CSRegistration.Empty
        }
    }

    @AnyThread
    fun pause()

    @AnyThread
    fun resume()

    @get:AnyThread
    val isListened: Boolean

    @AnyThread
    fun listen(function: (argument: T) -> Unit): CSRegistration

    @AnyThread
    fun fire(argument: T)

    @AnyThread
    fun clear()
}