package renetik.android.framework.store.property

import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.store.CSStore

interface CSStoreEventProperty<T> : CSEventProperty<T> {
    val store: CSStore
    val key: String
    fun set(store: CSStore, value: T) /// This set should go to base class
    fun saveTo(store: CSStore) = set(store, value)
    fun save() = set(store, value)
}