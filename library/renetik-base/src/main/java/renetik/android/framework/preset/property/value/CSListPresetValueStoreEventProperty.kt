package renetik.android.framework.preset.property.value

import renetik.android.framework.lang.CSId
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.property.value.CSListValueStoreEventProperty

class CSListPresetValueStoreEventProperty<T : CSId>(
    preset: CSPreset<*, *>, key: String, val values: Iterable<T>,
    default: List<T>, onChange: ((value: List<T>) -> Unit)?)
    : CSPresetValueStoreEventProperty<List<T>>(preset, key) {
    override val property = CSListValueStoreEventProperty(store, key, values, default, onChange)
}