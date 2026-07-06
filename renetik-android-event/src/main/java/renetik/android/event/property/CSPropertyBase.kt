package renetik.android.event.property

import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.event.lifecycle.CSModel

abstract class CSPropertyBase<T>(
    val parent: CSHasDestruct? = null,
    val onChange: ((value: T) -> Unit)? = null
) : CSModel(parent), CSProperty<T> {

    constructor(onChange: ((value: T) -> Unit)? = null)
            : this(parent = null, onChange = onChange)

    val eventChange = event<T>()

    override fun onChange(function: (T) -> Unit) = eventChange.listen(function)

    override fun fireChange() {
        val actualValue = value
        onChange?.invoke(actualValue)
        eventChange.fire(actualValue)
    }

    override fun value(newValue: T, fire: Boolean) {
        if (value == newValue) return
        value = newValue
        onValueChanged(newValue, fire)
    }

    protected open fun onValueChanged(newValue: T, fire: Boolean = true) {
        isChanged = true
        if (fire) fireChange()
    }

    protected open var isChanged = false

    override fun pause() {
        eventChange.pause()
        isChanged = false
    }

    override fun resume(fireChange: Boolean) {
        eventChange.resume()
        if (isChanged && fireChange) fireChange()
        isChanged = false
    }
}