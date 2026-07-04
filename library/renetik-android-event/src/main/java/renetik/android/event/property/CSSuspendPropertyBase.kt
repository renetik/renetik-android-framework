package renetik.android.event.property

import renetik.android.event.CSSuspendEvent
import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.event.lifecycle.CSModel

abstract class CSSuspendPropertyBase<T>(
    val parent: CSHasDestruct? = null,
    val onChange: suspend ((value: T) -> Unit) = {}
) : CSModel(parent), CSSuspendProperty<T> {

    constructor(onChange: suspend ((value: T) -> Unit) = {})
            : this(parent = null, onChange = onChange)

    val eventChange by lazy { CSSuspendEvent<T>() }

    override fun onChange(function: suspend (T) -> Unit) = eventChange.listen(function)

    override suspend fun fireChange() = value.let {
        onChange.invoke(it)
        eventChange.fire(it)
    }

    protected open suspend fun onValueChanged(newValue: T, fire: Boolean = true) {
        isChanged = true
        if (fire) fireChange()
    }

    protected open var isChanged = false

    override fun pause() {
        eventChange.pause()
        isChanged = false
    }

    override suspend fun resume(fireChange: Boolean) {
        eventChange.resume()
        if (isChanged && fireChange) fireChange()
        isChanged = false
    }
}