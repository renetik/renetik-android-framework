package renetik.android.store.extensions

import renetik.android.core.lang.ArgFun
import renetik.android.store.CSStore
import renetik.android.store.property.CSLateStoreProperty
import renetik.android.store.property.late.CSBooleanLateStoreProperty
import renetik.android.store.property.late.CSIntLateStoreProperty
import renetik.android.store.property.late.CSListItemLateStoreProperty
import renetik.android.store.property.late.CSStringLateStoreProperty
import renetik.android.store.property.listenLoad
import renetik.android.store.property.listenLoadOnce

fun CSStore.lateStringProperty(
    key: String,
    onChange: ArgFun<String>? = null
) = CSStringLateStoreProperty(this, key, onChange)

fun CSStore.dataLateStringProperty(
    key: String,
    onChange: ArgFun<String>? = null
) = lateStringProperty(key, onChange).listenLoad()

fun CSStore.lateIntProperty(
    key: String,
    onChange: ArgFun<Int>? = null
): CSLateStoreProperty<Int> =
    CSIntLateStoreProperty(this, key, onChange)

fun CSStore.dataLateIntProperty(
    key: String,
    onChange: ArgFun<Int>? = null
) = lateIntProperty(key, onChange).listenLoadOnce()

fun CSStore.lateBoolProperty(
    key: String,
    onChange: ArgFun<Boolean>? = null
): CSLateStoreProperty<Boolean> =
    CSBooleanLateStoreProperty(this, key, onChange)

fun <T> CSStore.lateListItemProperty(
    key: String, values: Iterable<T>,
    onChange: ArgFun<T>? = null
): CSLateStoreProperty<T> =
    CSListItemLateStoreProperty(this, key, values, onChange)

fun <T> CSStore.dataLateListItemProperty(
    key: String, values: Iterable<T>,
    onChange: ArgFun<T>? = null
): CSLateStoreProperty<T> =
    CSListItemLateStoreProperty(this, key, values, onChange).listenLoadOnce()

fun <T> CSStore.lateListItemProperty(
    key: String, values: Array<T>,
    onChange: ArgFun<T>? = null
): CSLateStoreProperty<T> =
    lateListItemProperty(key, values.asIterable(), onChange)
