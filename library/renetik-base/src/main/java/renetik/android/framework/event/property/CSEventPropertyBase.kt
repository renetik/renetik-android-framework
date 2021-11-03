package renetik.android.framework.event.property

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.CSModelBase
import renetik.android.framework.event.event
import renetik.android.framework.event.listen

abstract class CSEventPropertyBase<T>
    (parent: CSEventOwnerHasDestroy? = null,
     val onApply: ((value: T) -> Unit)? = null)
    : CSModelBase(parent), CSEventProperty<T> {

    constructor(onApply: ((value: T) -> Unit)? = null)
            : this(parent = null, onApply = onApply)

    protected val eventChange = event<T>()

    override fun onChange(function: (T) -> Unit) = eventChange.listen(function)

    override fun toString() = value.toString()

    fun apply(): CSEventProperty<T> = apply {
        val value = this.value
        onApply?.invoke(value)
        eventChange.fire(value)
    }
}