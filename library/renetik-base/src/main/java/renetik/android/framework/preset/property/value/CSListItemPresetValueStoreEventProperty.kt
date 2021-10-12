package renetik.android.framework.preset.property.value

import renetik.android.framework.preset.CSPreset
import renetik.android.framework.lang.property.CSListValuesProperty
import renetik.android.framework.store.property.value.CSListItemValueStoreEventProperty

class CSListItemPresetValueStoreEventProperty<T>(
    preset: CSPreset<*, *>,
    key: String,
    override val values: List<T>,
    getDefault: () -> T,
    onChange: ((value: T) -> Unit)?)
    : CSPresetValueStoreEventProperty<T>(preset, key, getDefault), CSListValuesProperty<T> {

    constructor(preset: CSPreset<*, *>, key: String, values: List<T>,
                default: T, onChange: ((value: T) -> Unit)?)
            : this(preset, key, values, getDefault = { default }, onChange)

    override val property =
        CSListItemValueStoreEventProperty(store, key, values, getDefault, onChange)
}