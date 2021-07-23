package renetik.android.framework.event

import renetik.android.framework.event.property.CSEventPropertyFunctions.property
import renetik.android.framework.event.property.isTrue
import renetik.android.framework.event.property.setFalse
import renetik.android.framework.event.property.setTrue
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
val CSActionInterface.isStopped get() = !isRunning
fun CSActionInterface.runIf(condition: Boolean) = condition.ifTrue { start() } ?: stop()

class CSAction(val id: String) : CSActionInterface {

    companion object {
        fun action(id: String): CSActionInterface = CSAction(id)
    }

    private val property = property(id, default = false)
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