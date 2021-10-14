package renetik.android.framework.preset.property.nullable

import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface

class CSFloatNullablePresetEventProperty(
    preset: CSPreset<*, *>,
    key: String,
    override val default: Float?,
    onChange: ((value: Float?) -> Unit)?)
    : CSNullablePresetEventProperty<Float>(preset, key, onChange) {
    override fun get(store: CSStoreInterface): Float? = store.getFloat(key)
    override fun set(store: CSStoreInterface, value: Float?) {
        if (value == null) store.clear(key) else store.set(key, value)
    }
}