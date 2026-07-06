package renetik.android.store.property.late

import renetik.android.event.lifecycle.onDestructed
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.cancelRegistrations
import renetik.android.store.CSStore
import renetik.android.store.type.CSJsonObjectStore
import kotlin.reflect.KClass

class CSJsonListLateStoreProperty<T : CSJsonObjectStore>(
    store: CSStore, override val key: String, val type: KClass<T>,
    onChange: ((value: List<T>) -> Unit)? = null
) : CSLateStorePropertyBase<List<T>>(store, key, onChange) {
    override fun get(store: CSStore): List<T>? = store.getJsonObjectList(key, type)
    override fun set(store: CSStore, value: List<T>) = store.setJsonObjectList(key, value)

    private val valuesOnChanged = mutableListOf<CSRegistration>()
        .also { onDestructed(it::cancelRegistrations) }

    override fun onLoadedValueChanged(value: List<T>?) {
        valuesOnChanged.cancelRegistrations()
        value?.forEach { valuesOnChanged + it.eventChanged.onChange { set(store, value) } }
    }
}