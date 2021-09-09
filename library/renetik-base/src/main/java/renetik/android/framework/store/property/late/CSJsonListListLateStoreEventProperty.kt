package renetik.android.framework.store.property.late

import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSStoreEventProperty
import renetik.android.framework.json.data.CSJsonMapStore
import renetik.android.framework.json.data.extensions.getListOfList
import kotlin.reflect.KClass

class CSJsonListListLateStoreEventProperty<T : CSJsonMapStore>(
    override val store: CSStoreInterface,
    override val key: String, val type: KClass<T>,
    onChange: ((value: List<List<T>>) -> Unit)? = null)
    : CSEventPropertyBase<List<List<T>>>(onChange), CSStoreEventProperty<List<List<T>>> {

    var _value: List<List<T>>? = null
        set(value) {
            field = value
            store.save(key, value!!)
        }

    override var value: List<List<T>>
        get() {
            if (_value == null) _value = store.getListOfList(key, type)
            return _value!!
        }
        set(value) {
            value(value)
        }

    override fun value(newValue: List<List<T>>, fire: Boolean) = apply {
        if (_value == newValue) return this
        if (store.has(key) && fire) eventBeforeChange.fire(value)
        _value = newValue
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }
}