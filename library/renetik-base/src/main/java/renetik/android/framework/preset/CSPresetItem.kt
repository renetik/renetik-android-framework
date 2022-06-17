package renetik.android.framework.preset

import renetik.android.core.lang.CSHasId
import renetik.android.framework.preset.property.CSPresetKeyData
import renetik.android.store.CSStore

interface CSPresetItem : CSHasId {
    val store: CSStore
    fun save(properties: Iterable<CSPresetKeyData>)
}