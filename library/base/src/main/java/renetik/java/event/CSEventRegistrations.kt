package renetik.java.event

import renetik.java.collections.list
import renetik.java.event.CSEvent.CSEventRegistration

class CSEventRegistrations() {

    private val registrations = list<CSEventRegistration>()
    private var active = true

    constructor(vararg registrations: CSEventRegistration) : this() {
        this.registrations.putAll(*registrations)
    }

    fun cancel() {
        for (reg in registrations) reg.cancel()
        registrations.clear()
    }

    fun addAll(vararg registrations: CSEventRegistration): CSEventRegistrations {
        this.registrations.putAll(*registrations)
        return this
    }

    fun add(registration: CSEventRegistration): CSEventRegistration {
        registration.isActive = active
        return registrations.put(registration)
    }

    fun setActive(active: Boolean) {
        this.active = active
        for (registration in registrations) registration.isActive = active
    }
}
