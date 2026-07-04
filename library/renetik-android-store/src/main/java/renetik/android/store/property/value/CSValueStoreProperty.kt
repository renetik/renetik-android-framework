package renetik.android.store.property.value

import renetik.android.core.lang.lazy.CSLazyNullableVar.Companion.lazyNullableVar
import renetik.android.event.property.CSPropertyBase
import renetik.android.store.CSStore
import renetik.android.store.property.CSStoreProperty

abstract class CSValueStoreProperty<T>(
    final override val store: CSStore,
    final override val key: String,
    onChange: ((value: T) -> Unit)? = null
) : CSPropertyBase<T>(onChange), CSStoreProperty<T> {

    abstract val default: T
    abstract override fun get(store: CSStore): T?
    protected var loadedValue: T? by lazyNullableVar(
        didSet = ::onLoadedValueChanged,
        initializer = { get(store) ?: default }
    )

    open fun onLoadedValueChanged(value: T?) = Unit

    override fun update() {
        val newValue = get(store)
        if (newValue == null) {
            if (loadedValue != default) {
                loadedValue = null
                onValueChanged(default)
            }
            loadedValue = null
        } else if (loadedValue != newValue) {
            loadedValue = newValue
            onValueChanged(newValue)
        }
    }

    override fun clear() {
        store.clear(key)
        if (!isDestructed) update()
    }

    override var value: T
        get() = loadedValue ?: default
        set(value) = value(value)

    override fun value(newValue: T, fire: Boolean) {
        if (loadedValue != newValue) saveValue(newValue, fire)
    }

    private fun saveValue(newValue: T, fire: Boolean) {
        loadedValue = newValue
        set(store, newValue)
        onValueChanged(newValue, fire)
    }

    override fun toString() = "key:$key ${super.toString()}"

    val isModified get() = value != default
}