package renetik.android.preset.property.value

import renetik.android.core.kotlin.toId
import renetik.android.core.lang.CSHasId
import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

class CSHasIdListValuePresetProperty<T : CSHasId>(
    parent: CSHasDestruct, preset: CSPreset<*, *>,
    key: String, val values: Iterable<T>,
    override val default: List<T>, onChange: ((value: List<T>) -> Unit)?
) : CSValuePresetProperty<List<T>>(parent, preset, key, onChange) {

    override fun get(store: CSStore) = store.getString(key)?.split(",")
        ?.mapNotNull { id -> values.find { it.id == id } }

    override fun set(store: CSStore, value: List<T>) =
        store.set(key, value.joinToString(",") { it.toId() })
}