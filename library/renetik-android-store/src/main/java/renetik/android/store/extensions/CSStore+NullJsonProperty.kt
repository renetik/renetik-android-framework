package renetik.android.store.extensions

import renetik.android.core.lang.ArgFun
import renetik.android.store.CSStore
import renetik.android.store.property.CSStoreProperty
import renetik.android.store.property.nullable.CSJsonListNullableStoreProperty
import renetik.android.store.property.nullable.CSJsonNullableStoreProperty
import renetik.android.store.type.CSJsonObjectStore
import kotlin.reflect.KClass

fun <T : CSJsonObjectStore> CSStore.nullJsonProperty(
    key: String, type: KClass<T>, default: T? = null,
    onChange: ArgFun<T?>? = null
): CSStoreProperty<T?> =
    CSJsonNullableStoreProperty(this, key, type, default, onChange)

inline fun <reified T : CSJsonObjectStore> CSStore.nullJsonProperty(
    key: String, default: T? = null, noinline onChange: ArgFun<T?>? = null
): CSStoreProperty<T?> = nullJsonProperty(key, T::class, default, onChange)

fun <T : CSJsonObjectStore> CSStore.nullJsonListProperty(
    key: String, listType: KClass<T>,
    onChange: ((value: List<T>?) -> Unit)? = null
): CSStoreProperty<List<T>?> =
    CSJsonListNullableStoreProperty(this, key, listType, onChange)

inline fun <reified T : CSJsonObjectStore> CSStore.nullJsonListProperty(
    key: String, noinline onChange: ArgFun<List<T>?>? = null
): CSStoreProperty<List<T>?> = nullJsonListProperty(key, T::class, onChange)