package renetik.android.framework.preset.property

import renetik.android.event.owner.CSEventOwnerHasDestroy
import renetik.android.event.listen
import renetik.android.event.registration.pause
import renetik.android.event.property.CSEventProperty
import renetik.android.event.property.CSEventPropertyBase
import renetik.android.event.property.CSEventPropertyFunctions.property
import renetik.android.event.register
import renetik.android.core.lang.property.isFalse
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStore

abstract class CSPresetEventPropertyBase<T>(
    override val parent: CSEventOwnerHasDestroy,
    final override val preset: CSPreset<*, *>,
    override val key: String,
    onChange: ((value: T) -> Unit)? = null
) : CSEventPropertyBase<T>(parent, onChange), CSPresetEventProperty<T> {

    protected abstract val default: T
    protected abstract var _value: T
    protected abstract fun get(store: CSStore): T?
    protected abstract fun set(store: CSStore, value: T)
    protected abstract fun load(): T
    protected abstract fun loadFrom(store: CSStore): T

    override fun saveTo(store: CSStore) = set(store, value)
    override val isFollowPreset: CSEventProperty<Boolean> = property(true)
    override val isModified: Boolean get() = value != loadFrom(preset.item.value.store)

    private val storeChanged = register(store.eventChanged.listen { onStoreChange() })
    private fun onStoreChange() {
        if (isFollowPreset.isFalse)
            parentStoreChangedIsFollowStoreFalseSaveToParentStore()
        else {
            val newValue = load()
            if (_value == newValue) return
            _value = newValue
            storeChanged.pause().use { onValueChanged(newValue) }
        }
    }

    private fun parentStoreChangedIsFollowStoreFalseSaveToParentStore() =
        store.eventChanged.pause().use { saveTo(store) }

    override fun value(newValue: T, fire: Boolean) {
        if (_value == newValue) return
        _value = newValue
        storeChanged.pause().use {
            onValueChanged(newValue, fire)
            saveTo(store)
        }
    }

    override var value: T
        get() = _value
        set(value) = value(value)

    override fun toString() = "${super.toString()} key:$key value:$value"
}