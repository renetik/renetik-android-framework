package renetik.android.json.store

import renetik.android.framework.store.CSStoreInterface
import renetik.android.json.data.CSJsonMapStore
import renetik.android.json.store.property.CSJsonListStoreEventProperty
import renetik.android.json.store.property.CSJsonStoreEventProperty
import renetik.android.json.store.property.CSJsonStoreNullableEventProperty
import kotlin.reflect.KClass

fun <T : CSJsonMapStore> CSStoreInterface.property(
    key: String, listType: KClass<T>, default: List<T>, onApply: ((value: List<T>) -> Unit)? = null
) = CSJsonListStoreEventProperty(this, key, listType, default, onApply)

fun <T : CSJsonMapStore> CSStoreInterface.property(
    key: String, createInstance: () -> T, default: List<T>,
    onApply: ((value: List<T>) -> Unit)? = null
) = CSJsonListStoreEventProperty(this, key, createInstance, default, onApply)

fun <T : CSJsonMapStore> CSStoreInterface.property(
    key: String, type: KClass<T>, default: T? = null, onApply: ((value: T?) -> Unit)? = null
) = CSJsonStoreNullableEventProperty(this, key, type, default, onApply)

fun <T : CSJsonMapStore> CSStoreInterface.property(
    key: String, type: KClass<T>, default: T, onApply: ((value: T) -> Unit)? = null
) = CSJsonStoreEventProperty(this, key, type, default, onApply)