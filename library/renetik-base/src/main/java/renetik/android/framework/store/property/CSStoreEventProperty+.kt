package renetik.android.framework.store.property

import renetik.android.framework.store.CSStoreInterface

fun <T> CSStoreEventProperty<T>.save(value: T) = save(store, value)
fun <T> CSStoreEventProperty<T>.save(store: CSStoreInterface) = save(store, value)
