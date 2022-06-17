package renetik.android.preset

import renetik.android.core.lang.CSHasId
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore

interface CSPresetItem : CSHasId {
    val store: CSStore
    fun save(properties: Iterable<CSPresetKeyData>)
}