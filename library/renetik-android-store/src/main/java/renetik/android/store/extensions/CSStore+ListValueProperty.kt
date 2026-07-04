package renetik.android.store.extensions

import renetik.android.core.lang.ArgFun
import renetik.android.core.lang.CSHasId
import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.event.lifecycle.parent
import renetik.android.store.CSStore
import renetik.android.store.property.CSStoreProperty
import renetik.android.store.property.listenLoad
import renetik.android.store.property.listenLoadOnce
import renetik.android.store.property.value.CSHasIdListValueStoreProperty

fun <T : CSHasId> CSStore.property(
    key: String, values: List<T>, default: List<T>,
    onChange: ArgFun<List<T>>? = null
): CSStoreProperty<List<T>> = CSHasIdListValueStoreProperty(
    this, key, values, default, onChange = onChange
)

fun <T : CSHasId> CSStore.dataProperty(
    key: String, values: List<T>, default: List<T>,
    onChange: ArgFun<List<T>>? = null
): CSStoreProperty<List<T>> = property(
    key, values, default, onChange
).listenLoadOnce()

fun <T : CSHasId> CSStore.property(
    parent: CSHasDestruct, key: String, values: List<T>, default: List<T>,
    onChange: ArgFun<List<T>>? = null
): CSStoreProperty<List<T>> = CSHasIdListValueStoreProperty(
    this, key, values, default, onChange = onChange
).parent(parent).listenLoad()

fun <T : CSHasId> CSStore.property(
    key: String, array: Array<T>, default: List<T>,
    onChange: ArgFun<List<T>>? = null
): CSStoreProperty<List<T>> = property(
    key, array.asList(), default, onChange
)

fun <T : CSHasId> CSStore.property(
    parent: CSHasDestruct, key: String, array: Array<T>, default: List<T>,
    onChange: ArgFun<List<T>>? = null
): CSStoreProperty<List<T>> = property(
    parent, key, array.asList(), default, onChange
)