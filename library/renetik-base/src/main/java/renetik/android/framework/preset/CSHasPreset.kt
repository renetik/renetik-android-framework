package renetik.android.framework.preset

import renetik.android.framework.CSEventOwnerHasDestroy

typealias Preset = CSPreset<*, out CSPresetItemList<*>>

interface CSHasPreset : CSEventOwnerHasDestroy {
    val preset: Preset
    val presetId: String
}