package renetik.android.preset.property

import renetik.android.event.owner.CSEventOwnerHasDestroy
import renetik.android.event.property.CSEventProperty

interface CSPresetEventProperty<T> : CSEventProperty<T>, CSPresetKeyData {
    val parent: CSEventOwnerHasDestroy
    val isFollowPreset: CSEventProperty<Boolean>
    val isModified: Boolean
    fun apply(): CSEventProperty<T>
}

