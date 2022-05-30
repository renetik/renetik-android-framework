package renetik.android.framework.preset

import renetik.android.framework.lang.CSHasId
import renetik.android.framework.preset.property.CSPresetKeyData
import renetik.android.framework.store.CSStore

interface CSPresetItem : CSHasId {
    val store: CSStore
//    fun delete()
    fun save(properties: Iterable<CSPresetKeyData>)
}