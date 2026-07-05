package renetik.android.store

import renetik.android.core.lang.ArgFun
import renetik.android.event.lifecycle.CSHasRegistrationsHasDestruct
import renetik.android.event.lifecycle.parent
import renetik.android.store.CSStore
import renetik.android.store.property.CSStoreProperty
import renetik.android.store.property.listenLoad
import renetik.android.store.property.listenLoadOnce
import renetik.android.store.property.value.CSJsonListValueStoreProperty
import renetik.android.store.property.value.CSJsonMutableListValueStoreProperty
import renetik.android.store.type.CSJsonObjectStore

inline fun <reified T : CSJsonObjectStore> CSStore.listProperty(
    key: String, noinline onChange: ArgFun<List<T>>? = null
): CSStoreProperty<List<T>> = CSJsonListValueStoreProperty(
    this, key, T::class, onChange
)

inline fun <reified T : CSJsonObjectStore> CSStore.dataListProperty(
    key: String, noinline onChange: ArgFun<List<T>>? = null
): CSStoreProperty<List<T>> = listProperty(key, onChange).listenLoadOnce()

inline fun <reified T : CSJsonObjectStore> CSStore.listProperty(
    parent: CSHasRegistrationsHasDestruct, key: String,
    noinline onChange: ArgFun<List<T>>? = null
) = CSJsonListValueStoreProperty(this, key, T::class, onChange)
    .parent(parent).listenLoad()

@JvmName("propertyMutableList")
inline fun <reified T : CSJsonObjectStore> CSStore.mutableListProperty(
    key: String, noinline onChange: ArgFun<MutableList<T>>? = null
): CSStoreProperty<MutableList<T>> = CSJsonMutableListValueStoreProperty(
    this, key, T::class, onChange
)

inline fun <reified T : CSJsonObjectStore> CSStore.dataMutableListProperty(
    key: String, noinline onChange: ArgFun<MutableList<T>>? = null
) = mutableListProperty(key, onChange).listenLoadOnce()

@JvmName("propertyMutableList")
inline fun <reified T : CSJsonObjectStore> CSStore.mutableListProperty(
    parent: CSHasRegistrationsHasDestruct, key: String,
    noinline onChange: ArgFun<MutableList<T>>? = null
): CSStoreProperty<MutableList<T>> = CSJsonMutableListValueStoreProperty(
    this, key, T::class, onChange
).parent(parent).listenLoad()