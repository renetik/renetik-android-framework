package renetik.android.framework.store.property.value

import renetik.android.framework.event.listen
import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.event.register
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSStoreEventProperty

abstract class CSValueStoreEventProperty<T>(
    final override val store: CSStoreInterface,
    final override val key: String,
    val listenStoreChanged: Boolean = false,
    onChange: ((value: T) -> Unit)? = null)
    : CSEventPropertyBase<T>(onChange), CSStoreEventProperty<T> {

    abstract val defaultValue: T
    protected abstract var _value: T
    abstract fun get(store: CSStoreInterface): T?
    var isStored = false

    fun load(): T {
        val value = get(store)
        return if (value != null) {
            isStored = true
            value
        } else defaultValue
    }

    private val storeEventChangedRegistration =
        if (listenStoreChanged) register(store.eventChanged.listen {
            val newValue = load()
            if (_value == newValue) return@listen
            _value = newValue
            onStoreChangeValueChange(newValue)
        }) else null

    private fun onStoreChangeValueChange(newValue: T) {
        storeEventChangedRegistration?.pause()?.use {
            onApply?.invoke(newValue)
            eventChange.fire(newValue)
        }
    }

    final override var value: T
        get() = _value
        set(value) = value(value)

    // Why do we need this logic with isStored,
    // When you have default value and set same it is stored but for what purpose ?
    // Best would be to remove and try live without it.
    override fun value(newValue: T, fire: Boolean) {
        if (_value == newValue) {
            if (!isStored) {
                isStored = true
                storeEventChangedRegistration?.pause()?.use { set(store, newValue) }
                    ?: run { set(store, newValue) }
            }
        } else {
            isStored = true
            _value = newValue
            storeEventChangedRegistration?.pause()?.use {
                set(store, newValue)
                onApply?.invoke(newValue)
                if (fire) eventChange.fire(newValue)
            } ?: run {
                set(store, newValue)
                onApply?.invoke(newValue)
                if (fire) eventChange.fire(newValue)
            }
        }
    }

    override fun toString() = "$key $value"
}