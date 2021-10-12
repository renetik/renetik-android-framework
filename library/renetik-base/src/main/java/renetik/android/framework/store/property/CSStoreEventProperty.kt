package renetik.android.framework.store.property

import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.store.CSStoreInterface

interface CSStoreEventProperty<T> : CSEventProperty<T> {
    val store: CSStoreInterface
    val key: String
    fun save(store: CSStoreInterface, value: T)
    fun save(store: CSStoreInterface) = save(store, value)
    fun save(value: T) = save(store, value)
    fun save() = save(store, value)
}