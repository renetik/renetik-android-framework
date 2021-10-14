package renetik.android.framework.preset.property.value

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface
import kotlin.reflect.KClass

class CSJsonListValuePresetEventProperty<T : CSJsonObject>(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    val type: KClass<T>,
    override val default: List<T> = emptyList(),
    onChange: ((value: List<T>) -> Unit)?)
    : CSValuePresetEventProperty<List<T>>(parent,preset, key, onChange) {
    override var _value = load()
    override fun get(store: CSStoreInterface) = store.getJsonList(key, type) ?: default
    override fun set(store: CSStoreInterface, value: List<T>) = store.set(key, value)
}