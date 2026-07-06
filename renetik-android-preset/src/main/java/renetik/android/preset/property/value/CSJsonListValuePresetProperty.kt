package renetik.android.preset.property.value

import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore
import renetik.android.store.type.CSJsonObjectStore
import kotlin.reflect.KClass

class CSJsonListValuePresetProperty<T : CSJsonObjectStore>(
    parent: CSHasDestruct,
    preset: CSPreset<*, *>,
    key: String,
    val type: KClass<T>,
    override val default: List<T> = emptyList(),
    onChange: ((value: List<T>) -> Unit)?)
    : CSValuePresetProperty<List<T>>(parent,preset, key, onChange) {
    override fun get(store: CSStore) = store.getJsonObjectList(key, type)
    override fun set(store: CSStore, value: List<T>) = store.setJsonObjectList(key, value)
}