package renetik.android.json.store

import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.store.CSStoreInterface
import renetik.android.json.data.CSJsonMap
import kotlin.reflect.KClass

class CSItemStoreEventProperty<T : CSJsonMap>(
    val store: CSStoreInterface, val key: String, type: KClass<T>,
    default: T? = null, onApply: ((value: T?) -> Unit)? = null
) : CSEventProperty<T?>(store.load(type, key, default), onApply) {
    override fun value(newValue: T?, fire: Boolean) {
        super.value(newValue, fire)
        save()
    }

    fun save() = store.save(key, value)
}