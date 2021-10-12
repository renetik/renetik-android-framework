package renetik.android.framework.preset

import renetik.android.framework.json.store.CSJsonStore
import renetik.android.framework.lang.CSId
import renetik.android.framework.store.property.CSStoreEventProperty

interface CSPresetItem : CSId {
    val store: CSJsonStore
    fun delete()
    val properties: List<CSStoreEventProperty<*>>
}