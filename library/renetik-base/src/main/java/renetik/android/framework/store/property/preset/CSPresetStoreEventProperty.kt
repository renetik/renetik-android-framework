package renetik.android.framework.store.property.preset

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSStoreEventProperty

//TODO This should have preset so we can move isModified here
interface CSPresetStoreEventProperty<T> : CSStoreEventProperty<T> {
    fun load(store: CSStoreInterface): T
    fun reload()

    fun load(): T = load(store)
}