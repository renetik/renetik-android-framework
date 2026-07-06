package renetik.android.preset.property.value

import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

class CSBooleanValuePresetProperty(
    parent: CSHasDestruct,
    preset: CSPreset<*, *>,
    key: String,
    override val default: Boolean,
    onChange: ((value: Boolean) -> Unit)?
) : CSValuePresetProperty<Boolean>(parent, preset, key, onChange) {
    override fun get(store: CSStore) = store.getBoolean(key)
    override fun set(store: CSStore, value: Boolean) = store.set(key, value)
}