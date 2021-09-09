package renetik.android.framework.store.property.preset

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSStoreEventProperty

interface CSPresetStoreEventProperty<T> : CSStoreEventProperty<T> {
    fun load(store: CSStoreInterface): T
    fun save(store: CSStoreInterface, value: T)
    fun reload()

    fun load(): T = load(store)
    fun save(value: T) = save(store, value)
    fun save(store: CSStoreInterface) = save(store, value)
}