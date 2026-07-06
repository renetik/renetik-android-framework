package renetik.android.preset.property.value

import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

class CSStringValuePresetProperty(
    parent: CSHasDestruct, preset: CSPreset<*, *>, key: String,
    private val getDefault: () -> String, onChange: ((value: String) -> Unit)?
) : CSValuePresetProperty<String>(parent, preset, key, onChange) {

    constructor(
        parent: CSHasDestruct, preset: CSPreset<*, *>, key: String,
        default: String, onChange: ((value: String) -> Unit)?
    ) : this(parent, preset, key, { default }, onChange)

    override val default: String get() = getDefault()
    override fun get(store: CSStore) = store.getString(key)
    override fun set(store: CSStore, value: String) = store.set(key, value)
}

