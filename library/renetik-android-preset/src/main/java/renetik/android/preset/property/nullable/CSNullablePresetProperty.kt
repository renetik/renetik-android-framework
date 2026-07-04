package renetik.android.preset.property.nullable

import renetik.android.event.lifecycle.CSHasRegistrationsHasDestruct
import renetik.android.preset.CSPreset
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.preset.property.CSPresetPropertyBase

abstract class CSNullablePresetProperty<T>(
    parent: CSHasRegistrationsHasDestruct,
    preset: CSPreset<*, *>,
    override val key: String,
    onChange: ((value: T?) -> Unit)?
) : CSPresetPropertyBase<T?>(parent, preset, key, onChange), CSPresetKeyData {
    override fun load(): T? = if (store.has(key)) get(store) else default
}