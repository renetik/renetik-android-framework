package renetik.android.framework.preset

import renetik.kotlin.collections.list
import renetik.kotlin.collections.putAll

val <PresetItem : CSPresetItem> CSPresetItemList<PresetItem>.items
    get() = list(defaultList).putAll(userList)

val CSPresetItemList<*>.count get() = defaultList.size + userList.size