package renetik.android.framework.preset.property.nullable

import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.preset.property.CSPresetEventProperty
import renetik.android.framework.store.CSStoreInterface

abstract class CSNullablePresetEventProperty<T>(
    final override val preset: CSPreset<*, *>,
    override val key: String,
    onChange: ((value: T?) -> Unit)?)
    : CSEventPropertyBase<T?>(onChange), CSPresetEventProperty<T?> {

    protected abstract val default: T?
    protected var _value: T? = null
    protected abstract fun get(store: CSStoreInterface): T?
    protected abstract fun set(store: CSStoreInterface, value: T?)

    override fun isModified() = value != (get(preset.current.value.store) ?: default)

    fun load(): T? = load(preset.store.value)
    fun load(store: CSStoreInterface): T? = get(store) ?: default?.also { set(store, it) }

    override fun reload(store: CSJsonObject) {
        val newValue = load(store)
        if (_value == newValue) return
        val before = _value
        _value = newValue
        onApply?.invoke(newValue)
        fireChange(before, newValue)
    }

    override fun save(store: CSJsonObject) = set(store, value)

    private val presetStoreInChange = preset.store.onChange {
        if (!preset.isReload) reload(it)
    }

    private var isLoaded = false
    override var value: T?
        get() {
            if (!isLoaded) {
                _value = load()
                isLoaded = true
            }
            return _value
        }
        set(value) = value(value)

    override fun value(newValue: T?, fire: Boolean) {
        if (value == newValue) return
        val before = value
        _value = newValue
        val newStore = CSJsonObject(preset.store.value)
        set(newStore, newValue)
        presetStoreInChange.pause().use { preset.store.value = newStore }
        onApply?.invoke(newValue)
        if (fire) fireChange(before, newValue)
    }
}