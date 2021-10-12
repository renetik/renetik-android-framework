package renetik.android.framework.preset.property.nullable

import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.property.nullable.CSFloatNullableStoreEventProperty

class CSFloatPresetNullableStoreEventProperty(
    preset: CSPreset<*, *>,
    key: String,
    default: Float?,
    onChange: ((value: Float?) -> Unit)?)
    : CSPresetNullableStoreEventProperty<Float>(preset, key, default) {
    override val property = CSFloatNullableStoreEventProperty(store, key, default, onChange)
}