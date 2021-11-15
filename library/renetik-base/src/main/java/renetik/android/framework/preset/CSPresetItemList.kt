package renetik.android.framework.preset

import renetik.java.util.currentTime

interface CSPresetItemList<PresetItem : CSPresetItem> {
    val default: List<PresetItem>
    val user: List<PresetItem>
    val items: List<PresetItem>
    fun put(item: PresetItem)
    fun remove(item: PresetItem)
    fun createPresetItem(
        title: String, isDefault: Boolean, id: String = "$currentTime"): PresetItem
}