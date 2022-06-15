package renetik.android.framework.preset.property

import renetik.android.framework.protocol.CSEventOwnerHasDestroy
import renetik.android.framework.event.property.CSEventProperty

interface CSPresetEventProperty<T> : CSEventProperty<T>, CSPresetKeyData {
    val parent: CSEventOwnerHasDestroy
    val isFollowPreset: CSEventProperty<Boolean>
    val isModified: Boolean
    fun apply(): CSEventProperty<T>
}

