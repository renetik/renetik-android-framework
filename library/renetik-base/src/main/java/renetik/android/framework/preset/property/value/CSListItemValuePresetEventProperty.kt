package renetik.android.framework.preset.property.value

import renetik.android.framework.lang.property.CSListValuesProperty
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.getValue
import renetik.kotlin.toId

class CSListItemValuePresetEventProperty<T>(
    preset: CSPreset<*, *>,
    key: String,
    override val values: List<T>,
    val getDefault: () -> T,
    onChange: ((value: T) -> Unit)?)
    : CSValuePresetEventProperty<T>(preset, key, onChange), CSListValuesProperty<T> {

    constructor(
        preset: CSPreset<*, *>, key: String,
        values: List<T>, default: T, onChange: ((value: T) -> Unit)? = null
    ) : this(preset, key, values, getDefault = { default }, onChange)

    override val default get() = getDefault()
    override var _value = load()
    override fun get(store: CSStoreInterface) = store.getValue(key, values)
    override fun set(store: CSStoreInterface, value: T) = store.set(key, value.toId())
}