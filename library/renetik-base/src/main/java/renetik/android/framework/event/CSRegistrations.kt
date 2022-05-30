package renetik.android.framework.event

import androidx.annotation.AnyThread
import renetik.kotlin.collections.removeIf
import java.lang.System.nanoTime

class CSRegistrations {
    private val registrations: MutableMap<Any, CSRegistration> = mutableMapOf()
    private var active = true

    @Synchronized
    @AnyThread
    fun cancel() {
        registrations.forEach { it.value.cancel() }
        registrations.clear()
    }

    @Synchronized
    @AnyThread
    fun addAll(vararg registrations: CSRegistration): CSRegistrations {
        registrations.forEach { add(it) }
        return this
    }

    @Synchronized
    @AnyThread
    fun add(registration: CSRegistration): CSRegistration {
        if (!registration.isActive) return registration
        registration.isActive = active
        registrations[nanoTime()] = registration
        return registration
    }

    @Synchronized
    @AnyThread
    fun add(key: Any, registration: CSRegistration): CSRegistration {
        if (!registration.isActive) return registration
        registrations[key]?.cancel()
        registrations[key] = registration
        registration.isActive = active
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
    fun remove(registration: CSRegistration) =
        registrations.removeIf { _, value -> value == registration }

    @Synchronized
    @AnyThread
    fun setActive(active: Boolean) {
        this.active = active
        for (registration in registrations) registration.value.isActive = active
    }
}