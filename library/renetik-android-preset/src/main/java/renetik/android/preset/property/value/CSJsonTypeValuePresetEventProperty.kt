package renetik.android.preset.property.value

import renetik.android.event.owner.CSEventOwnerHasDestroy
import renetik.android.store.json.CSStoreJsonObject
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore
import renetik.android.core.kotlin.reflect.createInstance
import kotlin.reflect.KClass

open class CSJsonTypeValuePresetEventProperty<T : CSStoreJsonObject>(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>, key: String, val type: KClass<T>,
    onChange: ((value: T) -> Unit)? = null)
    : CSValuePresetEventProperty<T>(parent, preset, key, onChange) {
    override val default get() = type.createInstance()!!
    override var _value = load()
    override fun get(store: CSStore) = store.getJsonObject(key, type)
    override fun set(store: CSStore, value: T) = store.set(key, value)
}


