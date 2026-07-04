package renetik.android.store.property.nullable

import renetik.android.event.lifecycle.onDestructed
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.cancelRegistrations
import renetik.android.store.CSStore
import renetik.android.store.property.value.CSValueStoreProperty
import renetik.android.store.type.CSJsonObjectStore
import kotlin.reflect.KClass

class CSJsonListNullableStoreProperty<T : CSJsonObjectStore>(
    store: CSStore, key: String, val type: KClass<T>,
    onChange: ((value: List<T>?) -> Unit)? = null
) : CSValueStoreProperty<List<T>?>(store, key, onChange) {
    override val default: List<T>? = null
    override fun get(store: CSStore): List<T>? = store.getJsonObjectList(key, type)
    override fun set(store: CSStore, value: List<T>?) = store.setJsonObjectList(key, value)

    private val valuesOnChanged = mutableListOf<CSRegistration>()
        .also { onDestructed(it::cancelRegistrations) }

    override fun onLoadedValueChanged(value: List<T>?) {
        valuesOnChanged.cancelRegistrations()
        value?.forEach { valuesOnChanged + it.eventChanged.onChange { set(store, value) } }
    }
}