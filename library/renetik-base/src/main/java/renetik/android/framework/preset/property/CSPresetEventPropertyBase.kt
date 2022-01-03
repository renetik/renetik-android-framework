package renetik.android.framework.preset.property

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.event.listen
import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.event.property.CSEventPropertyFunctions.property
import renetik.android.framework.event.register
import renetik.android.framework.lang.property.isFalse
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface

abstract class CSPresetEventPropertyBase<T>(
    val parent: CSEventOwnerHasDestroy,
    final override val preset: CSPreset<*, *>,
    override val key: String,
    onChange: ((value: T) -> Unit)? = null
) : CSEventPropertyBase<T>(parent, onChange), CSPresetEventProperty<T> {

    protected abstract val default: T
    protected abstract var _value: T
    protected abstract fun get(store: CSStoreInterface): T?
    protected abstract fun set(store: CSStoreInterface, value: T)
    protected abstract fun load(): T
    protected abstract fun loadFrom(store: CSStoreInterface): T

    override fun saveTo(store: CSStoreInterface) = set(store, value)
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
            storeChanged.pause().use {
                onApply?.invoke(newValue)
                eventChange.fire(newValue)
            }
        }
    }

    //TODO Experimental Change !!!
    private fun parentStoreChangedIsFollowStoreFalseSaveToParentStore() =
        store.eventChanged.pause().use { saveTo(store) }

    override fun value(newValue: T, fire: Boolean) {
        if (_value == newValue) return
        _value = newValue
        storeChanged.pause().use {
            onApply?.invoke(newValue)
            if (fire) eventChange.fire(newValue)
            saveTo(store)
        }
    }

    override var value: T
        get() = _value
        set(value) = value(value)

    override fun toString() = "${super.toString()} key:$key value:$value"
}