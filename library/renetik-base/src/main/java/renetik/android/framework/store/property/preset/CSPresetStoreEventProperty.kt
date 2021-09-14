package renetik.android.framework.store.property.preset

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSStoreEventProperty

interface CSPresetStoreEventProperty<T> : CSStoreEventProperty<T> {
    fun load(store: CSStoreInterface): T
    fun reload()

    fun load(): T = load(store)
}