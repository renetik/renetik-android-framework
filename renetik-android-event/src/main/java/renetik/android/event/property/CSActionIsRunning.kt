package renetik.android.event.property

import renetik.android.event.change.action
import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.plus

class CSActionIsRunning(
    parent: CSHasDestruct,
    vararg actions: CSProperty<Boolean>
) : CSPropertyWrapper<Boolean>(parent) {
    override val property = property(false)
    private val runningActions = mutableListOf<CSProperty<Boolean>>()

    init {
        add(actions)
    }

    private fun add(actions: Array<out CSProperty<Boolean>>) = actions.forEach { add(it) }

    fun add(action: CSProperty<Boolean>): CSRegistration {
        fun updateIsRunning(isRunning: Boolean) {
            if (isRunning) runningActions.add(action) else runningActions.remove(action)
            property.value(runningActions.size > 0)
        }

        val actionOnChange = this + action.action(::updateIsRunning)

        return CSRegistration(onCancel = {
            actionOnChange.cancel()
            runningActions.remove(action)
        })
    }
}