package renetik.android.framework.preset.property

import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface

interface CSPresetEventProperty<T> : CSEventProperty<T> {
    val preset: CSPreset<*, *>
    fun isModified(): Boolean
    val key: String
    fun reload(store: CSJsonObject)
    fun save(store: CSJsonObject)
}