package renetik.android.store.property.value

import renetik.android.core.kotlin.kClass
import renetik.android.core.lang.ArgFun
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.register
import renetik.android.store.CSStore
import renetik.android.store.type.CSJsonObjectStore

class CSJsonValueStoreProperty<T : CSJsonObjectStore>(
    store: CSStore, key: String,
    override val default: T,
    onChange: ArgFun<T>? = null
) : CSValueStoreProperty<T>(store, key, onChange) {

    override fun get(store: CSStore) = store.getJsonObject(key, default.kClass)
    override fun set(store: CSStore, value: T) = store.setJsonObject(key, value)

    private var onJsonObjectChanged: CSRegistration? = registerJsonObjectChanged(value)

    override fun onValueChanged(newValue: T, fire: Boolean) {
        super.onValueChanged(newValue, fire)
        onJsonObjectChanged = registerJsonObjectChanged(newValue)
    }

    private fun registerJsonObjectChanged(value: T) = register(
        onJsonObjectChanged, value.eventChanged.listen { set(store, value) }
    )
}