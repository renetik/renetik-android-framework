package renetik.android.framework.preset.property.value

import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.property.value.CSBooleanValueStoreEventProperty

class CSBooleanPresetValueStoreEventProperty(
    preset: CSPreset<*, *>,
    key: String,
    default: Boolean,
    onChange: ((value: Boolean) -> Unit)?)
    : CSPresetValueStoreEventProperty<Boolean>(preset, key) {
    override val property = CSBooleanValueStoreEventProperty(store, key, default, onChange)
}