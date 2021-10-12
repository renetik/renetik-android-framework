package renetik.android.framework.preset.property.value

import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.property.value.CSDoubleValueStoreEventProperty

class CSDoublePresetValueStoreEventProperty(
    preset: CSPreset<*, *>,
    key: String,
    default: Double,
    onChange: ((value: Double) -> Unit)?)
    : CSPresetValueStoreEventProperty<Double>(preset, key) {
    override val property = CSDoubleValueStoreEventProperty(store, key, default, onChange)
}