package renetik.android.framework.preset.property.value

import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.property.value.CSFloatValueStoreEventProperty

class CSFloatPresetValueStoreEventProperty(
    preset: CSPreset<*, *>,
    key: String,
    default: Float,
    onChange: ((value: Float) -> Unit)?)
    : CSPresetValueStoreEventProperty<Float>(preset, key, default) {
    override val property = CSFloatValueStoreEventProperty(store, key, default, onChange)
}