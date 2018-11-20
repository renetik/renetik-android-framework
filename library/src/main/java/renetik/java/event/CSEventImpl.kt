package renetik.java.event

import renetik.java.extensions.exception
import renetik.android.lang.CSLog.logError
import renetik.java.collections.list
import renetik.java.event.CSEvent.CSEventRegistration
import renetik.java.lang.CSLang

class CSEventImpl<T> : CSEvent<T> {

    private var registrations = list<EventRegistrationImpl>()
    private var toRemove = list<EventRegistrationImpl>()
    private var toAdd = list<EventRegistrationImpl>()
    private var running: Boolean = false

    override fun run(listener: (CSEventRegistration, T) -> Unit): CSEventRegistration {
        val registration = EventRegistrationImpl(listener)
        if (running) toAdd.add(registration)
        else registrations.add(registration)
        return registration
    }

    override fun fire(argument: T) {
        if (running) logError(exception("Event run while _running"))
        if (registrations.isEmpty()) return
        running = true
        for (registration in registrations) registration.onEvent(argument)
        for (registration in toRemove) registrations.delete(registration)
        toRemove.clear()
        registrations.addAll(toAdd)
        toAdd.clear()
        running = false
    }

    override fun clear() = registrations.clear()

    internal inner class EventRegistrationImpl(private val listener: (CSEventRegistration, T) -> Unit) : CSEventRegistration {

        override var isActive = CSLang.YES

        private var canceled = CSLang.NO

        override fun cancel() {
            if (canceled) return
            val index = registrations.indexOf(this)
            if (index >= 0) {
                if (running) toRemove.add(this)
                else registrations.removeAt(index)
            } else logError(exception("Listener not found"))
            canceled = CSLang.YES
        }

        override fun event() = this@CSEventImpl

        fun onEvent(argument: T) {
            if (isActive) listener(this, argument)
        }
    }

}