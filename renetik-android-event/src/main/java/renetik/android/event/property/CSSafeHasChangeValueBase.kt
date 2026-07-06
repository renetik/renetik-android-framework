@file:OptIn(ExperimentalAtomicApi::class)

package renetik.android.event.property

import renetik.android.core.lang.ArgFun
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.dispatch.onMain
import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.event.lifecycle.CSModel
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

internal open class CSSafeHasChangeValueBase<T>(
    parent: CSHasDestruct,
    initialValue: T,
    private val onChange: ArgFun<T>? = null
) : CSModel(parent), CSSafeHasChangeValue<T> {
    private val _value = AtomicReference(initialValue)
    private val eventUnsafeChange = event<T>()
    private val eventChange = event<T>()

    override var value: T
        get() = _value.load()
        set(value) = _value.store(value)

    override fun onChange(function: (T) -> Unit) = eventChange.listen(function)
    override fun onUnsafeChange(function: (T) -> Unit) = eventUnsafeChange.listen(function)

    fun value(newValue: T, force: Boolean = false) {
        if (newValue != _value.exchange(newValue) || force) {
            eventUnsafeChange.fire(newValue)
            onMain {
                if (registrations.isActive) {
                    onChange?.invoke(newValue)
                    eventChange.fire(newValue)
                }
            }
        }
    }
}