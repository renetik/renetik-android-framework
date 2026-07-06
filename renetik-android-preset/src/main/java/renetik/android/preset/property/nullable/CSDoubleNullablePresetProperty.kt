package renetik.android.preset.property.nullable

import renetik.android.event.lifecycle.CSHasRegistrationsHasDestruct
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

class CSDoubleNullablePresetProperty(
    parent: CSHasRegistrationsHasDestruct,
    preset: CSPreset<*, *>,
    key: String,
    override val default: Double?,
    onChange: ((value: Double?) -> Unit)?)
    : CSNullablePresetProperty<Double>(parent, preset, key, onChange) {
    override fun get(store: CSStore): Double? = store.getDouble(key)
    override fun set(store: CSStore, value: Double?) = store.set(key, value)
}