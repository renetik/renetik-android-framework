package renetik.android.framework.event

import androidx.annotation.UiThread
import java.io.Closeable

interface CSEvent<T> {

    companion object {
        @JvmName("eventWithType")
        fun <T> event(): CSEvent<T> = CSEventImpl()

        fun event(): CSEvent<Unit> = CSEventImpl()
    }

    fun pause(): Closeable

    fun resume()

    val isListened: Boolean

    fun add(@UiThread listener: (registration: CSRegistration,
                                 argument: T) -> Unit): CSRegistration

    fun add(@UiThread listener: CSEventListener<T>): CSRegistration

    fun cancel(listener: CSEventListener<T>)

    fun fire(argument: T)

    fun clear()

    @Deprecated("Just for debugging")
    val registrations: List<CSRegistration>
}