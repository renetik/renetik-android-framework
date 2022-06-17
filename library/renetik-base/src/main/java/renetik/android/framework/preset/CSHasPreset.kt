package renetik.android.framework.preset

import renetik.android.core.lang.CSHasId
import renetik.android.event.owner.CSEventOwnerHasDestroy

typealias Preset = CSPreset<*, out CSPresetItemList<*>>

interface CSHasPreset : CSEventOwnerHasDestroy {
    val preset: Preset
    val presetId: String
}

interface CSHasPresetHasId : CSHasPreset, CSHasId