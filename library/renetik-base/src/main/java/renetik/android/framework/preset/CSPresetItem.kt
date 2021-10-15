package renetik.android.framework.preset

import renetik.android.framework.lang.CSHasId
import renetik.android.framework.preset.property.CSPresetEventProperty
import renetik.android.framework.store.CSStoreInterface

interface CSPresetItem : CSHasId {
    val store: CSStoreInterface
    fun delete()
    fun save(properties: Iterable<CSPresetEventProperty<*>>)
}