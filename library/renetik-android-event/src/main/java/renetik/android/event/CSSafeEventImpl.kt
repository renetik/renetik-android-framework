package renetik.android.event

import androidx.annotation.AnyThread
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.event.registration.CSRegistration

class CSSafeEventImpl<T>(
    parent: CSHasDestruct
) : CSSafeEvent<T> {
    private val eventUnsafeChange = event<T>()
    private val eventChange = event<T>()

    init {
        eventChange.onMain(parent)
    }

    @AnyThread
    override fun pause() {
        eventUnsafeChange.pause()
        eventChange.pause()
    }

    @AnyThread
    override fun resume() {
        eventUnsafeChange.resume()
        eventChange.resume()
    }

    override val isListened: Boolean
        @AnyThread get() = eventUnsafeChange.isListened || eventChange.isListened

    @AnyThread
    override fun listen(function: (T) -> Unit): CSRegistration =
        eventChange.onChange(function)

    @AnyThread
    override fun onChange(function: (T) -> Unit): CSRegistration =
        listen(function)

    @AnyThread
    override fun onUnsafeChange(function: (T) -> Unit): CSRegistration =
        eventUnsafeChange.listen(function)

    @AnyThread
    override fun fire(argument: T) {
        if (!isListened) return
        eventUnsafeChange.fire(argument)
        eventChange.fire(argument)
    }

    @AnyThread
    override fun clear() {
        eventUnsafeChange.clear()
        eventChange.clear()
    }
}
