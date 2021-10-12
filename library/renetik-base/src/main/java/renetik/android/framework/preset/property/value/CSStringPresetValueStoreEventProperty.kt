package renetik.android.framework.preset.property.value

import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.property.value.CSStringValueStoreEventProperty

class CSStringPresetValueStoreEventProperty(
    preset: CSPreset<*, *>,
    key: String,
    default: String,
    onChange: ((value: String) -> Unit)?)
    : CSPresetValueStoreEventProperty<String>(preset, key, default) {
    override val property =
        CSStringValueStoreEventProperty(store, key, default, onChange)
}

