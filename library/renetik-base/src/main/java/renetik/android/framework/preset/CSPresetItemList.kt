package renetik.android.framework.preset

import renetik.java.util.currentTime
import renetik.kotlin.collections.list
import renetik.kotlin.collections.putAll

interface CSPresetItemList<PresetItem : CSPresetItem> {
    val defaultList: List<PresetItem>
    val userList: List<PresetItem>
    fun put(item: PresetItem)
    fun remove(item: PresetItem)
    fun createPresetItem(title: String, isDefault: Boolean,
                         id: String = "$currentTime"): PresetItem
    fun reload()
}

val <PresetItem : CSPresetItem> CSPresetItemList<PresetItem>.items
    get() = list(defaultList).putAll(userList)

val CSPresetItemList<*>.count get() = defaultList.size + userList.size