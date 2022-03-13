package renetik.android.framework.preset.property

import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.event.register
import renetik.android.framework.lang.property.connect
import renetik.android.framework.store.CSStoreInterface

val CSPresetEventProperty<*>.store: CSStoreInterface get() = preset.store

fun <T> CSPresetEventProperty<T>.followPresetIf(property: CSEventProperty<Boolean>) = apply {
    parent.register(isFollowPreset.connect(property))
}