package renetik.android.store.extensions

import renetik.android.core.lang.ArgFun
import renetik.android.store.CSStore
import renetik.android.store.property.CSStoreProperty
import renetik.android.store.property.listenLoad
import renetik.android.store.property.nullable.CSListItemNullableStoreProperty

fun <T> CSStore.nullListItemProperty(
    key: String, getValues: () -> List<T>, getDefault: () -> T?,
    onChange: ArgFun<T?>? = null
) = CSListItemNullableStoreProperty(
    this, key, getValues, getDefault, onChange)

fun <T> CSStore.nullListItemProperty(
    key: String, values: List<T>, getDefault: () -> T?,
    onChange: ArgFun<T?>? = null
): CSStoreProperty<T?> =
    nullListItemProperty(key, { values }, getDefault, onChange)

fun <T> CSStore.nullListItemProperty(
    key: String, values: List<T>, default: T? = null,
    onChange: ArgFun<T?>? = null
) = nullListItemProperty(key, { values }, { default }, onChange)

fun <T> CSStore.dataNullListItemProperty(
    key: String, values: List<T>, default: T? = null,
    onChange: ArgFun<T?>? = null
) = nullListItemProperty(key, values, default, onChange).listenLoad()

fun <T> CSStore.nullListItemProperty(
    key: String, getList: () -> List<T>, defaultIndex: Int,
    onChange: ArgFun<T?>? = null
): CSStoreProperty<T?> =
    nullListItemProperty(key, getList, { getList()[defaultIndex] }, onChange)

fun <T> CSStore.nullListItemProperty(
    key: String, list: List<T>, defaultIndex: Int,
    onChange: ArgFun<T?>? = null
): CSStoreProperty<T?> =
    nullListItemProperty(key, list, list[defaultIndex], onChange)

fun <T> CSStore.nullListItemProperty(
    key: String, array: Array<T>, default: T? = null,
    onChange: ArgFun<T?>? = null
): CSStoreProperty<T?> =
    nullListItemProperty(key, array.asList(), default, onChange)

fun <T> CSStore.nullListItemProperty(
    key: String, array: Array<T>, defaultIndex: Int,
    onChange: ArgFun<T?>? = null
): CSStoreProperty<T?> =
    nullListItemProperty(key, array.asList(), array[defaultIndex], onChange)