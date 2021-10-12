package renetik.android.framework.preset.property.nullable

import renetik.android.framework.preset.CSPreset
import renetik.android.framework.lang.property.CSListValuesProperty
import renetik.android.framework.store.property.nullable.CSListItemNullableStoreEventProperty

class CSListItemPresetNullableStoreEventProperty<T>(
    preset: CSPreset<*, *>,
    key: String,
    override val values: List<T>,
    default: T?,
    onChange: ((value: T?) -> Unit)?)
    : CSPresetNullableStoreEventProperty<T>(preset, key, default), CSListValuesProperty<T?> {
    override val property =
        CSListItemNullableStoreEventProperty(store, key, values, default, onChange)
}