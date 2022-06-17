package renetik.android.event

import renetik.android.core.logging.CSLog.logError
import renetik.android.core.kotlin.collections.hasItems
import renetik.android.core.kotlin.collections.list
import renetik.android.core.kotlin.exception
import renetik.android.event.registration.CSRegistration
import java.io.Closeable

class CSEventImpl<T> : CSEvent<T> {

    private val listeners = list<CSEventListener<T>>()
    private var toRemove = list<CSEventListener<T>>()
    private var toAdd = list<CSEventListener<T>>()
    private var running = false
    private var paused = false

    override fun add(listener: (CSRegistration, T) -> Unit) = add(EventListenerImpl(listener))

    override fun add(listener: CSEventListener<T>): CSRegistration {
        if (running) toAdd.add(listener)
        else listeners.add(listener)
        return listener
    }

    override fun fire(argument: T) {
        if (paused) return
        if (running)
            logError(exception("Event run while running"))
        if (listeners.isEmpty()) return
        running = true
        for (listener in listeners) listener.onEvent(argument)
        if (toRemove.hasItems) {
            for (listener in toRemove) listeners.delete(listener)
            toRemove.clear()
        }
        if (toAdd.hasItems) {
            listeners.addAll(toAdd)
            toAdd.clear()
        }
        running = false
    }

    override fun clear() = listeners.clear()
    override val isListened get() = listeners.hasItems

    internal inner class EventListenerImpl(
        private val listener: (CSRegistration, T) -> Unit) :
        CSEventListener<T> {
        override var isActive = true
        private var canceled = false
        override fun cancel() {
            if (canceled) return
            isActive = false
            cancel(this)
            canceled = true
        }

        override fun onEvent(argument: T) {
            if (isActive) listener(this, argument)
        }
    }

    override fun cancel(listener: CSEventListener<T>) {
        val index = listeners.indexOf(listener)
        if (index >= 0) {
            if (running) toRemove.add(listener)
            else listeners.removeAt(index)
        } else logError("Listener not found")
    }

    override val registrations get() = listeners

    override fun pause(): Closeable {
        paused = true
        return Closeable { paused = false }
    }

    override fun resume() {
        paused = false
    }
}
