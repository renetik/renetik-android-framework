package renetik.android.preset.property.value

import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

class CSFloatValuePresetProperty(
    parent: CSHasDestruct,
    preset: CSPreset<*, *>,
    key: String,
    override val default: Float,
    onChange: ((value: Float) -> Unit)?
) : CSValuePresetProperty<Float>(parent, preset, key, onChange) {
    override fun get(store: CSStore) = store.getFloat(key)
    override fun set(store: CSStore, value: Float) = store.set(key, value)
}