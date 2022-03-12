package renetik.android.framework.preset

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.lang.CSHasId

typealias Preset = CSPreset<*, out CSPresetItemList<*>>

interface CSHasPreset : CSEventOwnerHasDestroy {
    val preset: Preset
    val presetId: String
}

interface CSHasPresetHasId : CSHasPreset, CSHasId