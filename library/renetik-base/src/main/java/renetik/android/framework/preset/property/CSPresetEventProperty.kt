package renetik.android.framework.preset.property

import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.store.CSStoreInterface

interface CSPresetEventProperty<T> : CSEventProperty<T>, CSPresetKeyData {
    val isFollowPreset: CSEventProperty<Boolean>
    val isModified: Boolean
    fun apply(): CSEventProperty<T>
}

val CSPresetEventProperty<*>.store: CSStoreInterface get() = preset.store