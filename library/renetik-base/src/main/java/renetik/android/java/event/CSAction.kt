package renetik.android.java.event

import renetik.android.primitives.ifTrue

interface CSActionInterface {
    val isObserved: Boolean
    fun onObserveChange(function: (CSActionInterface) -> Unit): CSEvent.CSEventRegistration
    val isRunning: Boolean
    fun onChange(function: (CSActionInterface) -> Unit): CSEvent.CSEventRegistration
    fun start()
    fun stop()
}

fun CSActionInterface.toggle() = apply { if (isRunning) stop() else start() }
fun CSActionInterface.runIf(condition: Boolean) = condition.ifTrue { start() } ?: stop()

class CSAction(val id: String, default: Boolean = false) : CSActionInterface {

    companion object {
        fun action(id: String, default: Boolean = false): CSActionInterface = CSAction(id, default)
    }

    private val property = CSEventPropertyFunctions.property(id, default)
    private val eventIsObserved = event()
    private var observerCount = 0

    override val isObserved get() = observerCount > 0

    override fun onObserveChange(function: (CSActionInterface) -> Unit) =
        eventIsObserved.listen { function(this) }

    override val isRunning get() = property.isTrue

    override fun onChange(function: (CSActionInterface) -> Unit): CSEvent.CSEventRegistration {
        observerCount++
        if (observerCount == 1) eventIsObserved.fire()
        return CSActionOnChangeEventRegistration(property.onChange { function(this) })
    }

    override fun start() {
        property.setTrue()
    }

    override fun stop() {
        property.setFalse()
    }

    inner class CSActionOnChangeEventRegistration(
        private val registration: CSEvent.CSEventRegistration) : CSEvent.CSEventRegistration {
        override var isActive
            get() = registration.isActive
            set(value) {
                registration.isActive = value
            }

        override fun cancel() {
            registration.cancel()
            observerCount--
            if (observerCount == 0) eventIsObserved.fire()
        }

        override fun event() = registration.event()
    }
}