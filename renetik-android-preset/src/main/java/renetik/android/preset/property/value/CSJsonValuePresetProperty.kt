package renetik.android.preset.property.value

import renetik.android.core.kotlin.reflect.createInstance
import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore
import renetik.android.store.type.CSJsonObjectStore
import kotlin.reflect.KClass

open class CSJsonValuePresetProperty<T : CSJsonObjectStore>(
    parent: CSHasDestruct,
    preset: CSPreset<*, *>, key: String,
    val type: KClass<T>,
    onChange: ((value: T) -> Unit)? = null
) : CSValuePresetProperty<T>(parent, preset, key, onChange) {
    override val default get() = type.createInstance()!!
    override fun get(store: CSStore) = store.getJsonObject(key, type)
    override fun set(store: CSStore, value: T) = store.setJsonObject(key, value)
}


