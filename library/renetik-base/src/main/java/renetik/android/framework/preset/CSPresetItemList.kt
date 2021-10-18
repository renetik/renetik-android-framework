package renetik.android.framework.preset

interface CSPresetItemList<PresetItem : CSPresetItem> {
    val default: List<PresetItem>
    val user: List<PresetItem>
    val items: List<PresetItem>
    fun put(item: PresetItem)
    fun remove(item: PresetItem)
    fun createPresetItem(title: String, isDefault: Boolean): PresetItem
}