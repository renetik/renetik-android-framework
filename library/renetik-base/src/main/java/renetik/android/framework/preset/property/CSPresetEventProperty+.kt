package renetik.android.framework.preset.property

import renetik.android.event.property.CSEventProperty
import renetik.android.event.property.connect
import renetik.android.event.register
import renetik.android.framework.store.CSStore

val CSPresetEventProperty<*>.store: CSStore get() = preset.store

fun <T> CSPresetEventProperty<T>.followPresetIf(property: CSEventProperty<Boolean>) = apply {
    parent.register(isFollowPreset.connect(property))
}