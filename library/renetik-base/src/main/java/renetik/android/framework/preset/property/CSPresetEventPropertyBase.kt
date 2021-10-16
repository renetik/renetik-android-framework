package renetik.android.framework.preset.property

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.event.listen
import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.event.property.CSEventPropertyFunctions.property
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface

abstract class CSPresetEventPropertyBase<T>(
    parent: CSEventOwnerHasDestroy,
    final override val preset: CSPreset<*, *>,
    override val key: String,
    onChange: ((value: T) -> Unit)? = null
) : CSEventPropertyBase<T>(parent, onChange), CSPresetEventProperty<T> {

    final override val isModified = property(false)

    protected abstract val default: T
    protected abstract var _value: T
    protected abstract fun get(store: CSStoreInterface): T?
    protected abstract fun set(store: CSStoreInterface, value: T)

    fun load(): T = load(preset.store.value)
    abstract fun load(store: CSStoreInterface): T

    override fun save(store: CSJsonObject) = set(store, value)

    protected open fun updateIsModified() =
        isModified.value(value != (get(preset.item.value.store) ?: default))

    var isNewValue = false

    open fun reloadLoad(store: CSJsonObject) {
        val newValue = load(store)
        if (_value == newValue) return
        _value = newValue
        isNewValue = true
    }

    protected open fun reloadUpdate() {
        if (!isNewValue) return
        updateIsModified()
        onApply?.invoke(_value)
        eventChange.fire(_value)
        isNewValue = false
    }

    protected val presetStoreInChange = register(preset.store.onChange {
        if (!preset.isReload) reload(it)
    })

    protected open fun reload(store: CSJsonObject) {
        val newValue = load(store)
        if (_value == newValue) return
        _value = newValue
        updateIsModified()
        onApply?.invoke(newValue)
        eventChange.fire(newValue)
    }

    override fun value(newValue: T, fire: Boolean) {
        if (_value == newValue) return
        _value = newValue
//        save(preset.store.value)
        val newStore = CSJsonObject(preset.store.value)
        set(newStore, newValue)
        presetStoreInChange.pause().use { preset.store.value = newStore }
        updateIsModified()
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }

    init {
        register(preset.eventReload.listen {
            reloadUpdate()
        })
        register(preset.item.onChange {
            updateIsModified()
        })
    }

    override var value: T
        get() = _value
        set(value) = value(value)

    override fun toString() = "$key $value"
}