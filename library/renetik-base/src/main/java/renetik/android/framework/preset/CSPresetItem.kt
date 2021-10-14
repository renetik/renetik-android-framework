package renetik.android.framework.preset

import renetik.android.framework.lang.CSId
import renetik.android.framework.preset.property.CSPresetEventProperty
import renetik.android.framework.store.CSStoreInterface

interface CSPresetItem : CSId {
    val store: CSStoreInterface
    fun delete()
    fun save(properties: Iterable<CSPresetEventProperty<*>>)
}