package renetik.android.framework.event

import renetik.android.framework.event.CSEvent.CSEventRegistration
import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.collections.put
import renetik.android.java.extensions.collections.putAll

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

    fun cancel(registration: CSEventRegistration) {
        registration.cancel()
        registrations.remove(registration)
    }

    fun setActive(active: Boolean) {
        this.active = active
        for (registration in registrations) registration.isActive = active
    }

    val size get() = registrations.size
}
