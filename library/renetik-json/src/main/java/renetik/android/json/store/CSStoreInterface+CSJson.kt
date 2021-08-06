package renetik.android.json.store

import renetik.android.framework.store.CSStoreInterface
import renetik.android.json.data.CSJsonMap
import renetik.android.json.store.property.CSItemStoreEventProperty
import renetik.android.json.store.property.CSListStoreEventProperty
import kotlin.reflect.KClass

fun <T : CSJsonMap> CSStoreInterface.property(
    key: String, listType: KClass<T>, default: List<T>, onApply: ((value: List<T>) -> Unit)? = null
) = CSListStoreEventProperty(this, key, listType, default, onApply)

fun <T : CSJsonMap> CSStoreInterface.property(
    key: String, createInstance: () -> T, default: List<T>,
    onApply: ((value: List<T>) -> Unit)? = null
) = CSListStoreEventProperty(this, key, createInstance, default, onApply)

fun <T : CSJsonMap> CSStoreInterface.property(
    key: String, type: KClass<T>, default: T? = null, onApply: ((value: T?) -> Unit)? = null
) = CSItemStoreEventProperty(this, key, type, default, onApply)