package renetik.android.java.event

import renetik.android.java.collections.list
import renetik.android.java.event.CSEvent.CSEventRegistration
import renetik.android.lang.CSLang.YES

class CSEventRegistrations() {

    private val registrations = list<CSEventRegistration>()
    private var active = YES

    constructor(vararg registrations: CSEventRegistration) : this() {
        this.registrations.append(*registrations)
    }

    fun cancel() {
        for (reg in registrations) reg.cancel()
        registrations.clear()
    }

    fun addAll(vararg registrations: CSEventRegistration): CSEventRegistrations {
        this.registrations.append(*registrations)
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
