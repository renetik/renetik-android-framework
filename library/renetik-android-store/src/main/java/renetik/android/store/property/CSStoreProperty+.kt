@file:Suppress("NOTHING_TO_INLINE")

package renetik.android.store.property

import renetik.android.event.change.onChange
import renetik.android.event.change.onChangeOnce
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.plus
import renetik.android.store.CSStore

fun <T> CSStoreProperty<T>.saveTo(store: CSStore) = set(store, value)
fun <T> CSStoreProperty<T>.save() = set(store, value)
fun <T> CSStoreProperty<T>.save(value: T) = set(store, value)

inline fun <T, V> T.listenLoad() where T : CSStoreProperty<V>, T : CSHasRegistrations = apply {
    this + store.eventLoaded.onChange(::update)
}

inline fun <T, V> T.listenChange() where T : CSStoreProperty<V>, T : CSHasRegistrations = apply {
    this + store.eventChanged.onChange(::update)
}

inline fun <T : CSStoreProperty<*>> T.listenLoad(parent: CSHasRegistrations) = apply {
    parent + store.eventLoaded.onChange(::update)
}

inline fun <T : CSStoreProperty<*>> T.listenLoadOnce() = apply {
    store.eventLoaded.onChangeOnce(parent = null, ::update)
}