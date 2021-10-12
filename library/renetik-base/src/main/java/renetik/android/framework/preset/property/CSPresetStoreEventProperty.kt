package renetik.android.framework.preset.property

import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.property.CSStoreEventProperty

interface CSPresetStoreEventProperty<T>
    : CSStoreEventProperty<T> {
    val preset: CSPreset<*, *>
    fun isModified(): Boolean
}