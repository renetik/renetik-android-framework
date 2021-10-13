package renetik.android.framework.store.property.value

import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.event.listen
import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSStoreEventProperty

abstract class CSValueStoreEventProperty<T>(
    store: CSStoreInterface,
    final override val key: String,
    onChange: ((value: T) -> Unit)? = null)
    : CSEventPropertyBase<T>(onChange), CSStoreEventProperty<T> {

    var storeEventLoadedRegistration: CSEventRegistration

    init {
        storeEventLoadedRegistration = store.eventLoaded.listen(::reload)
    }

    override var store: CSStoreInterface = store
        set(value) {
            storeEventLoadedRegistration.cancel()
            field = value
            storeEventLoadedRegistration = store.eventLoaded.listen(::reload)
            reload()
        }

    fun reload() = value(load())
    fun load() = get(store) ?: defaultValue.also { set(store, it) }

    abstract val defaultValue: T

    protected abstract var _value: T

    final override var value: T
        get() = _value
        set(value) = value(value)

    abstract fun get(store: CSStoreInterface): T?

    override fun value(newValue: T, fire: Boolean) {
        if (_value == newValue) return
        val before = _value
        _value = newValue
        set(store, value)
        onApply?.invoke(newValue)
        if (fire) fireChange(before, newValue)
    }

    override fun toString() = "$key $value"

    @Deprecated("This is wrong")
    override fun apply() = apply {
        val before = value
        val value = this.value
        onApply?.invoke(value)
        fireChange(before, value)
    }
}