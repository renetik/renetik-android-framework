package renetik.android.store.property.nullable

import renetik.android.core.lang.ArgFun
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.register
import renetik.android.json.obj.clone
import renetik.android.store.CSStore
import renetik.android.store.property.value.CSValueStoreProperty
import renetik.android.store.type.CSJsonObjectStore
import kotlin.reflect.KClass

class CSJsonNullableStoreProperty<T : CSJsonObjectStore>(
    store: CSStore, key: String, val type: KClass<T>,
    override val default: T? = null,
    onChange: ArgFun<T?>? = null
) : CSValueStoreProperty<T?>(store, key, onChange) {

    override fun get(store: CSStore) = store.getJsonObject(key, type)

    override fun set(store: CSStore, value: T?) =
        value?.let { store.setJsonObject(key, it) } ?: store.clear(key)

    override var value: T?
        get() {
            if (loadedValue == null)
                loadedValue = default?.clone()
            return loadedValue
        }
        set(value) = value(value)

    private var onJsonObjectChanged: CSRegistration? = registerJsonObjectChanged(value)

    override fun onValueChanged(newValue: T?, fire: Boolean) {
        super.onValueChanged(newValue, fire)
        onJsonObjectChanged = registerJsonObjectChanged(newValue)
    }

    private fun registerJsonObjectChanged(value: T?) = register(
        onJsonObjectChanged, value?.eventChanged?.listen { set(store, value) }
    )
}