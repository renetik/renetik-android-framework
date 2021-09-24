package renetik.android.framework.event.property

import renetik.android.framework.event.event
import renetik.android.framework.event.listen

data class CSPropertyChange<T>(val before: T, val after: T)

abstract class CSEventPropertyBase<T>(
    protected val onApply: ((value: T) -> Unit)? = null) : CSEventProperty<T> {
    private val eventChange = event<CSPropertyChange<T>>()

    override fun onChanged(function: (before: T, after: T) -> Unit) =
        eventChange.listen { function(it.before, it.after) }

    override fun onChange(function: (T) -> Unit) = eventChange.listen { function(it.after) }

    override fun apply() = apply {
        val value = this.value
        onApply?.invoke(value)
//        eventChange.fire(CSPropertyChange<T>(null, value))
    }

    protected fun fireChange(before: T, after: T) =
        eventChange.fire(CSPropertyChange<T>(before, after))

    override fun toString() = value.toString()
}