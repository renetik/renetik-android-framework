package renetik.android.framework.preset.property.nullable

import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.property.nullable.CSIntNullableStoreEventProperty

class CSIntPresetNullableStoreEventProperty(
    preset: CSPreset<*, *>,
    key: String,
    default: Int?,
    onChange: ((value: Int?) -> Unit)?)
    : CSPresetNullableStoreEventProperty<Int>(preset, key) {
    override val property = CSIntNullableStoreEventProperty(store, key, default, onChange)
}


