package renetik.android.preset.property.value

import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.preset.CSPreset
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.preset.property.CSPresetPropertyBase

abstract class CSValuePresetProperty<T>(
    parent: CSHasDestruct,
    preset: CSPreset<*, *>,
    override val key: String,
    onChange: ((value: T) -> Unit)? = null,
) : CSPresetPropertyBase<T>(parent, preset, key, onChange), CSPresetKeyData {
    override fun load(): T = get(store) ?: default
}
