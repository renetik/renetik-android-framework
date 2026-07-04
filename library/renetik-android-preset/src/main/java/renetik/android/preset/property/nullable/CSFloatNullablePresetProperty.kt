package renetik.android.preset.property.nullable

import renetik.android.event.lifecycle.CSHasRegistrationsHasDestruct
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

class CSFloatNullablePresetProperty(
    parent: CSHasRegistrationsHasDestruct,
    preset: CSPreset<*, *>,
    key: String,
    override val default: Float?,
    onChange: ((value: Float?) -> Unit)?)
    : CSNullablePresetProperty<Float>(parent, preset, key, onChange) {
    override fun get(store: CSStore): Float? = store.getFloat(key)
    override fun set(store: CSStore, value: Float?) = store.set(key, value)
}