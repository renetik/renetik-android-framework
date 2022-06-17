package renetik.android.framework.store.json

import renetik.android.framework.store.CSStore
import renetik.android.framework.store.property.nullable.CSJsonTypeNullableStoreEventProperty
import renetik.android.framework.store.property.value.CSJsonListValueStoreEventProperty
import renetik.android.framework.store.property.value.CSJsonTypeValueStoreEventProperty
import kotlin.reflect.KClass

fun <T : CSStoreJsonObject> CSStore.property(
    key: String, listType: KClass<T>, default: List<T>,
    onApply: ((value: List<T>) -> Unit)? = null
) = CSJsonListValueStoreEventProperty(this,
    key, listType, default, listenStoreChanged = false, onApply)

fun <T : CSStoreJsonObject> CSStore.property(
    key: String, type: KClass<T>, default: T? = null,
    onApply: ((value: T?) -> Unit)? = null
) = CSJsonTypeNullableStoreEventProperty(this, key, type, default, onApply)

fun <T : CSStoreJsonObject> CSStore.property(
    key: String, type: KClass<T>, onApply: ((value: T) -> Unit)? = null
) = CSJsonTypeValueStoreEventProperty(this, key, type, listenStoreChanged = false, onApply)