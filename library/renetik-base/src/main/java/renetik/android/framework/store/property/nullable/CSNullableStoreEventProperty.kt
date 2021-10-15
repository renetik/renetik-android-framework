package renetik.android.framework.store.property.nullable

import renetik.android.framework.event.listen
import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSStoreEventProperty

abstract class CSNullableStoreEventProperty<T>(
    final override val store: CSStoreInterface,
    final override val key: String,
    val defaultValue: T?,
    onApply: ((value: T?) -> Unit)? = null)
    : CSEventPropertyBase<T?>(onApply), CSStoreEventProperty<T?> {

    protected var _value: T? = null
    abstract fun get(store: CSStoreInterface): T?

    fun load(): T? = load(store)
    fun load(store: CSStoreInterface): T? = get(store) ?: defaultValue

    private val storeEventChangedRegistration = store.eventChanged.listen {
        _value = load()
    }

    var isLoaded = false
    override var value: T?
        get() {
            if (!isLoaded) {
                _value = load()
                isLoaded = true
            }
            return _value
        }
        set(value) = value(value)

    override fun value(newValue: T?, fire: Boolean) {
        if (value == newValue) return
        _value = newValue
        storeEventChangedRegistration.pause().use { set(store, newValue) }
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }
}