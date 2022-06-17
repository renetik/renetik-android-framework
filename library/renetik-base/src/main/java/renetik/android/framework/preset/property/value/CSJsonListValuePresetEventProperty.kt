package renetik.android.framework.preset.property.value

import renetik.android.event.owner.CSEventOwnerHasDestroy
import renetik.android.store.json.CSStoreJsonObject
import renetik.android.framework.preset.CSPreset
import renetik.android.store.CSStore
import kotlin.reflect.KClass

class CSJsonListValuePresetEventProperty<T : CSStoreJsonObject>(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    val type: KClass<T>,
    override val default: List<T> = emptyList(),
    onChange: ((value: List<T>) -> Unit)?)
    : CSValuePresetEventProperty<List<T>>(parent,preset, key, onChange) {
    override var _value = load()
    override fun get(store: CSStore) = store.getJsonObjectList(key, type) ?: default
    override fun set(store: CSStore, value: List<T>) = store.set(key, value)
}