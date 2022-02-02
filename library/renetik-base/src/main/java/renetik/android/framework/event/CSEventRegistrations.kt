package renetik.android.framework.event

import androidx.annotation.AnyThread
import renetik.kotlin.collections.list
import renetik.kotlin.collections.putAll

class CSEventRegistrations() {

    private val registrations = list<CSEventRegistration>()
    private var active = true

    constructor(vararg registrations: CSEventRegistration) : this() {
        this.registrations.putAll(*registrations)
    }

    @Synchronized
    @AnyThread
    fun cancel() {
        for (reg in registrations) reg.cancel()
        registrations.clear()
    }

    @Synchronized
    @AnyThread
    fun addAll(vararg registrations: CSEventRegistration): CSEventRegistrations {
        this.registrations.putAll(*registrations)
        return this
    }

    @Synchronized
    @AnyThread
    fun add(registration: CSEventRegistration): CSEventRegistration {
        registration.isActive = active
        return registrations.put(registration)
    }

    @Synchronized
    @AnyThread
    fun cancel(registration: CSEventRegistration) {
        registration.cancel()
        remove(registration)
    }

    @Synchronized
    @AnyThread
    fun remove(registration: CSEventRegistration) {
        registrations.remove(registration)
    }

    @Synchronized
    @AnyThread
    fun setActive(active: Boolean) {
        this.active = active
        for (registration in registrations) registration.isActive = active
    }
}
