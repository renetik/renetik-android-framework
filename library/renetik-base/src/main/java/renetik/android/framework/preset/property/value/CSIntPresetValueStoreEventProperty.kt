package renetik.android.framework.preset.property.value

import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.property.value.CSIntValueStoreEventProperty

class CSIntPresetValueStoreEventProperty(
    preset: CSPreset<*, *>,
    key: String,
    default: Int,
    onChange: ((value: Int) -> Unit)?)
    : CSPresetValueStoreEventProperty<Int>(preset, key, default) {
    override val property = CSIntValueStoreEventProperty(store, key, default, onChange)
}


