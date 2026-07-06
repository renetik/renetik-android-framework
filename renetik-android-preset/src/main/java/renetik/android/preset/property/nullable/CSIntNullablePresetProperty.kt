package renetik.android.preset.property.nullable

import renetik.android.event.lifecycle.CSHasRegistrationsHasDestruct
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

class CSIntNullablePresetProperty(
    parent: CSHasRegistrationsHasDestruct,
    preset: CSPreset<*, *>,
    key: String,
    override val default: Int?,
    onChange: ((value: Int?) -> Unit)?
) : CSNullablePresetProperty<Int>(parent, preset, key, onChange) {
    override fun get(store: CSStore): Int? = store.getInt(key)
    override fun set(store: CSStore, value: Int?) = store.set(key, value)
}


