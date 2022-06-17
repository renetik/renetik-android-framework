package renetik.android.framework.event.property

import renetik.android.framework.base.CSBase
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.listen
import renetik.android.framework.protocol.CSHasDestroy

//CSEventProperty in iOS
abstract class CSEventPropertyBase<T>(
    parent: CSHasDestroy? = null,
    val onApply: ((value: T) -> Unit)? = null)
    : CSBase(parent), CSEventProperty<T> {

    constructor(onApply: ((value: T) -> Unit)? = null)
            : this(parent = null, onApply = onApply)

    val eventChange = event<T>()

    override fun onChange(function: (T) -> Unit) = eventChange.listen(function)

    override fun toString() = value.toString()

    fun apply(): CSEventProperty<T> = apply { onValueChanged(this.value) }

    protected fun onValueChanged(newValue: T, fire: Boolean = true) {
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }
}