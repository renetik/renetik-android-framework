package renetik.android.framework.preset

import renetik.android.event.owner.CSEventOwnerHasDestroy
import renetik.android.core.lang.CSHasId

typealias Preset = CSPreset<*, out CSPresetItemList<*>>

interface CSHasPreset : CSEventOwnerHasDestroy {
    val preset: Preset
    val presetId: String
}

interface CSHasPresetHasId : CSHasPreset, CSHasId