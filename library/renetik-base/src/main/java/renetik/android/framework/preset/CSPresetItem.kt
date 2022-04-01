package renetik.android.framework.preset

import renetik.android.framework.lang.CSHasId
import renetik.android.framework.preset.property.CSPresetKeyData
import renetik.android.framework.store.CSStoreInterface

interface CSPresetItem : CSHasId {
    val store: CSStoreInterface
//    fun delete()
    fun save(properties: Iterable<CSPresetKeyData>)
}