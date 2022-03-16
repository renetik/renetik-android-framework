package renetik.android.framework.event

import androidx.annotation.AnyThread
import renetik.kotlin.collections.list
import renetik.kotlin.collections.putAll

class CSEventRegistrations() {

    private val registrations = list<CSRegistration>()
    private val registrationsMap = mutableMapOf<Any, CSRegistration>()
    private var active = true

    constructor(vararg registrations: CSRegistration) : this() {
        this.registrations.putAll(*registrations)
    }

    @Synchronized
    @AnyThread
    fun cancel() {
        for (reg in registrations) reg.cancel()
        registrations.clear()

        for (reg in registrationsMap) reg.value.cancel()
        registrationsMap.clear()
    }

    @Synchronized
    @AnyThread
    fun addAll(vararg registrations: CSRegistration): CSEventRegistrations {
        this.registrations.putAll(*registrations)
        return this
    }

    @Synchronized
    @AnyThread
    fun add(registration: CSRegistration): CSRegistration {
        registration.isActive = active
        return registrations.put(registration)
    }

    @Synchronized
    @AnyThread
    fun add(key: Any, registration: CSRegistration): CSRegistration {
        registration.isActive = active
        registrationsMap[key]?.cancel()
        registrationsMap[key] = registration
        return registration
    }

    @Synchronized
    @AnyThread
    fun cancel(registration: CSRegistration) {
        registration.cancel()
        remove(registration)
    }

    @Synchronized
    @AnyThread
    fun remove(registration: CSRegistration) {
        registrations.remove(registration)
    }

    @Synchronized
    @AnyThread
    fun setActive(active: Boolean) {
        this.active = active
        for (registration in registrations) registration.isActive = active
    }
}
