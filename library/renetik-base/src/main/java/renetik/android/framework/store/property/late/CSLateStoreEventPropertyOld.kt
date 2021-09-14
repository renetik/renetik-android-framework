package renetik.android.framework.store.property.late

import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSStoreEventProperty
import renetik.android.framework.store.property.save

@Deprecated("...")
abstract class CSLateStoreEventPropertyOld<T>(
    override val store: CSStoreInterface,
    override val key: String, onChange: ((value: T) -> Unit)?)
    : CSEventPropertyBase<T>(onChange), CSStoreEventProperty<T> {

    protected open var _value: T
        get() = load()
        set(value) = save(value)

    abstract fun load(): T

    override fun value(newValue: T, fire: Boolean) = apply {
        if (store.has(key) && value == newValue) return this
        if (store.has(key) && fire) eventBeforeChange.fire(value)
        _value = newValue
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }

    override var value: T
        get() = _value
        set(value) {
            value(value)
        }

}