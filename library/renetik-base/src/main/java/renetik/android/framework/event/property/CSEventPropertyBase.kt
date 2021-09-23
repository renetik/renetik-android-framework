package renetik.android.framework.event.property

import renetik.android.framework.event.event
import renetik.android.framework.event.listen

abstract class CSEventPropertyBase<T>(
    protected val onApply: ((value: T) -> Unit)? = null) : CSEventProperty<T> {
    protected val eventBeforeChange = event<T>()
    protected val eventChange = event<T>()

    override fun onBeforeChange(value: (T) -> Unit) = eventBeforeChange.listen(value)
    override fun onChange(function: (T) -> Unit) = eventChange.listen(function)

    override fun apply() = apply {
        val value = this.value
        onApply?.invoke(value)
        eventChange.fire(value)
    }

    override fun toString() = value.toString()
}