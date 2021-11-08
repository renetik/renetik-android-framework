package renetik.android.framework.preset.property

import renetik.android.framework.event.CSHasDestroy
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface

interface CSPresetKeyData : CSHasDestroy {
    val preset: CSPreset<*, *>
    val key: String
    fun saveTo(store: CSStoreInterface)
}