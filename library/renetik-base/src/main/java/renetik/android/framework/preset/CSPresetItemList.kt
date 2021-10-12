package renetik.android.framework.preset

interface CSPresetItemList<PresetItem : CSPresetItem> {
    val path: String
    val items: List<PresetItem>
    fun put(preset: PresetItem)
    fun remove(preset: PresetItem)
}