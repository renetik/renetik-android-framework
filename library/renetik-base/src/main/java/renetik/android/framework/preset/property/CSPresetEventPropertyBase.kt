package renetik.android.framework.preset.property

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface

abstract class CSPresetEventPropertyBase<T>(
    parent: CSEventOwnerHasDestroy,
    final override val preset: CSPreset<*, *>,
    override val key: String,
    onChange: ((value: T) -> Unit)? = null
) : CSEventPropertyBase<T>(parent, onChange), CSPresetEventProperty<T> {

    protected abstract val default: T
    protected abstract var _value: T
    protected abstract fun get(store: CSStoreInterface): T?
    protected abstract fun set(store: CSStoreInterface, value: T)

    fun load(): T = load(preset.store.value)
    abstract fun load(store: CSStoreInterface): T

    override fun save(store: CSJsonObject) = set(store, value)

    init {
        preset.store.onChange {
            value(load())
        }
    }

    override fun value(newValue: T, fire: Boolean) {
        if (_value == newValue) return
        _value = newValue
        save(preset.store.value)
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }

    override var value: T
        get() = _value
        set(value) = value(value)

    override fun toString() = "$key $value"
}