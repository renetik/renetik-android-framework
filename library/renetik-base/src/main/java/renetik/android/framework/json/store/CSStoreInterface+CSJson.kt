package renetik.android.framework.json.store

import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.nullable.CSJsonTypeNullableStoreEventProperty
import renetik.android.framework.store.property.value.CSJsonListValueStoreEventProperty
import renetik.android.framework.store.property.value.CSJsonTypeValueStoreEventProperty
import kotlin.reflect.KClass

fun <T : CSJsonObject> CSStoreInterface.property(
    key: String, listType: KClass<T>, default: List<T>,
    onApply: ((value: List<T>) -> Unit)? = null
) = CSJsonListValueStoreEventProperty(this,
    key, listType, default, listenStoreChanged = false, onApply)

fun <T : CSJsonObject> CSStoreInterface.property(
    key: String, type: KClass<T>, default: T? = null,
    onApply: ((value: T?) -> Unit)? = null
) = CSJsonTypeNullableStoreEventProperty(this, key, type, default, onApply)

fun <T : CSJsonObject> CSStoreInterface.property(
    key: String, type: KClass<T>, onApply: ((value: T) -> Unit)? = null
) = CSJsonTypeValueStoreEventProperty(this, key, type, listenStoreChanged = false, onApply)