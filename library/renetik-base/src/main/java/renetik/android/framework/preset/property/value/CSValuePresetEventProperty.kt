package renetik.android.framework.preset.property.value

import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.preset.property.CSPresetEventProperty
import renetik.android.framework.store.CSStoreInterface

abstract class CSValuePresetEventProperty<T>(
    final override val preset: CSPreset<*, *>,
    override val key: String,
    onChange: ((value: T) -> Unit)? = null
) : CSEventPropertyBase<T>(onChange), CSPresetEventProperty<T> {

    protected val store get() = preset.store.value

    protected abstract val default: T
    protected abstract var _value: T
    protected abstract fun get(store: CSStoreInterface): T?
    protected abstract fun set(store: CSStoreInterface, value: T)

    override fun isModified() = value != (get(preset.current.value.store) ?: default)

    fun load(): T = load(store)
    fun load(store: CSStoreInterface): T = get(store) ?: default.also { set(store, it) }
    override fun reload(store: CSJsonObject) {
        val newValue = load(store)
        if (_value == newValue) return
        val before = _value
        _value = newValue
        onApply?.invoke(newValue)
        fireChange(before, newValue)
    }

    override fun save(store: CSJsonObject) = set(store, value)

    init {
        preset.store.onChange {
            if (!preset.isReload) reload(store)
        }
    }

    override var value: T
        get() = _value
        set(value) = value(value)

    override fun value(newValue: T, fire: Boolean) {
        if (_value == newValue) return
        val before = _value
        _value = newValue
        onValueChanged(newValue, fire, before)
    }

    protected open fun onValueChanged(newValue: T, fire: Boolean, before: T) {
        val newStore = CSJsonObject(store)
        set(newStore, newValue)
        preset.store.value = newStore
        onApply?.invoke(newValue)
        if (fire) fireChange(before, newValue)
    }

    override fun toString() = "$key $value"

    @Deprecated("This is wrong")
    override fun apply() = apply {
        val before = value
        val value = this.value
        onApply?.invoke(value)
        fireChange(before, value)
    }
}