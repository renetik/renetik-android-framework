package renetik.android.framework.preset.property.value

import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface

class CSFloatValuePresetEventProperty(
    preset: CSPreset<*, *>,
    key: String,
    override val default: Float,
    onChange: ((value: Float) -> Unit)?)
    : CSValuePresetEventProperty<Float>(preset, key, onChange) {
    override var _value = load()
    override fun get(store: CSStoreInterface) = store.getFloat(key)
    override fun set(store: CSStoreInterface, value: Float) = store.set(key, value)
}