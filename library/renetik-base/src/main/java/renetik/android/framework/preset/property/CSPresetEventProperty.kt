package renetik.android.framework.preset.property

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.event.register
import renetik.android.framework.lang.property.connect
import renetik.android.framework.store.CSStoreInterface

interface CSPresetEventProperty<T> : CSEventProperty<T>, CSPresetKeyData {
    val parent: CSEventOwnerHasDestroy
    val isFollowPreset: CSEventProperty<Boolean>
    val isModified: Boolean
    fun apply(): CSEventProperty<T>
}

