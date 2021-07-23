package renetik.android.framework.event

import renetik.android.framework.event.CSEvent.CSEventRegistration
import renetik.android.java.extensions.collections.delete
import renetik.android.java.extensions.collections.hasItems
import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.exception
import renetik.android.logging.CSLog.logError

class CSEventImpl<T> : CSEvent<T> {

    private var registrations = list<EventRegistrationImpl>()
    private var toRemove = list<EventRegistrationImpl>()
    private var toAdd = list<EventRegistrationImpl>()
    private var running = false

    override fun add(listener: (CSEventRegistration, T) -> Unit): CSEventRegistration {
        val registration = EventRegistrationImpl(listener)
        if (running) toAdd.add(registration)
        else registrations.add(registration)
        return registration
    }

    override fun fire(argument: T) {
        if (running)
            logError(exception("Event run while running"))
        if (registrations.isEmpty()) return
        running = true
        for (registration in registrations) registration.onEvent(argument)
        if (toRemove.hasItems) {
            for (registration in toRemove) registrations.delete(registration)
            toRemove.clear()
        }
        if (toAdd.hasItems) {
            registrations.addAll(toAdd)
            toAdd.clear()
        }
        running = false
    }

    override fun clear() = registrations.clear()

    internal inner class EventRegistrationImpl(private val listener: (CSEventRegistration, T) -> Unit) :
        CSEventRegistration {

        override var isActive = true

        private var canceled = false

        override fun cancel() {
            if (canceled) return
            isActive = false
            val index = registrations.indexOf(this)
            if (index >= 0) {
                if (running) toRemove.add(this)
                else registrations.removeAt(index)
            }
            else logError("Listener not found")
            canceled = true
        }

        override fun event() = this@CSEventImpl

        fun onEvent(argument: T) {
            if (isActive) listener(this, argument)
        }
    }

}