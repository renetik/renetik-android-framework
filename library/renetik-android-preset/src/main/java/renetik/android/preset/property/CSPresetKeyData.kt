package renetik.android.preset.property

import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

interface CSPresetKeyData : CSHasDestruct {
    val preset: CSPreset<*, *>
    val key: String
    fun saveTo(store: CSStore)
    fun clearKeyData()
    fun clear()
    fun onStoreLoaded()
    fun isTrackedModifiedIn(store: CSStore): Boolean = false
}