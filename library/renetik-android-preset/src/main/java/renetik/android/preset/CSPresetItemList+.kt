package renetik.android.preset

import renetik.android.core.kotlin.collections.list
import renetik.android.core.kotlin.collections.putAll

val <PresetItem : CSPresetItem> CSPresetItemList<PresetItem>.items
    get() = list(defaultList).putAll(userList)

val CSPresetItemList<*>.count get() = defaultList.size + userList.size