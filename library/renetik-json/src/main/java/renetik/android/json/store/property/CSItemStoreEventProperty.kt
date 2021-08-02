package renetik.android.json.store.property

import renetik.android.framework.event.property.CSEventPropertyImpl
import renetik.android.framework.store.CSStoreInterface
import renetik.android.json.data.CSJsonMap
import renetik.android.json.store.load
import renetik.android.json.store.save
import kotlin.reflect.KClass

class CSItemStoreEventProperty<T : CSJsonMap>(
    val store: CSStoreInterface, val key: String, type: KClass<T>,
    default: T? = null, onApply: ((value: T?) -> Unit)? = null
) : CSEventPropertyImpl<T?>(store.load(type, key, default), onApply) {
    override fun value(newValue: T?, fire: Boolean) {
        super.value(newValue, fire)
        save()
    }

    fun save() = store.save(key, value)
}