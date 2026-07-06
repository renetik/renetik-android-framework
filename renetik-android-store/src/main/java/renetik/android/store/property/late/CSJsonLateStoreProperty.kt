package renetik.android.store.property.late

import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.register
import renetik.android.store.CSStore
import renetik.android.store.type.CSJsonObjectStore
import kotlin.reflect.KClass

class CSJsonLateStoreProperty<T : CSJsonObjectStore>(
    store: CSStore, override val key: String, val type: KClass<T>,
    onChange: ((value: T) -> Unit)? = null
) : CSLateStorePropertyBase<T>(store, key, onChange) {
    override fun get(store: CSStore): T? = store.getJsonObject(key, type)
    override fun set(store: CSStore, value: T) = store.setJsonObject(key, value)

    private var onJsonObjectChanged: CSRegistration? = null

    override fun onValueChanged(newValue: T, fire: Boolean) {
        super.onValueChanged(newValue, fire)
        onJsonObjectChanged = registerJsonObjectChanged(newValue)
    }

    private fun registerJsonObjectChanged(value: T) = register(
        onJsonObjectChanged, value.eventChanged.listen { set(store, value) }
    )
}