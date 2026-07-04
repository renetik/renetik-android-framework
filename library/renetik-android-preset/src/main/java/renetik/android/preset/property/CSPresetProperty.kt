package renetik.android.preset.property

import renetik.android.event.property.CSProperty
import renetik.android.store.CSStore
import renetik.android.store.property.CSStoreProperty

interface CSPresetProperty<T> : CSStoreProperty<T>, CSPresetKeyData {
    val default: T
    override val store get() = preset.store
    val isFollowPreset: CSProperty<Boolean>
    override fun clearKeyData() = super.clear()
    override fun clear() = super.clear()
}

fun <T : CSPresetProperty<*>> T.isModifiedIn(store: CSStore) = if (store.has(key))
    value != get(store) else value != default

