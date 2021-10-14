package renetik.android.framework.preset.property.value

import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface

class CSDoubleValuePresetEventProperty(
    preset: CSPreset<*, *>,
    key: String,
    override val default: Double,
    onChange: ((value: Double) -> Unit)?)
    : CSValuePresetEventProperty<Double>(preset, key, onChange) {
    override var _value = load()
    override fun get(store: CSStoreInterface) = store.getDouble(key)
    override fun set(store: CSStoreInterface, value: Double) = store.set(key, value)
}