package renetik.android.framework.preset.property

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.event.listen
import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface

abstract class CSPresetEventPropertyBase<T>(
    parent: CSEventOwnerHasDestroy,
    final override val preset: CSPreset<*, *>,
    override val key: String,
    onChange: ((value: T) -> Unit)? = null
) : CSEventPropertyBase<T>(parent, onChange), CSPresetEventProperty<T> {

    protected abstract val default: T
    protected abstract var _value: T
    protected abstract fun get(store: CSStoreInterface): T?
    protected abstract fun set(store: CSStoreInterface, value: T)

    abstract fun loadFrom(store: CSStoreInterface): T
    fun load(): T = loadFrom(preset.store.value)

    override fun saveTo(store: CSJsonObject) = set(store, value)

    lateinit var presetStoreValueEventChanged: CSEventRegistration

    init {
        registerOnStoreChange(parent)
        parent.register(preset.store.onChange {
            onStoreChange()
            registerOnStoreChange(parent)
        })
    }

    private fun registerOnStoreChange(parent: CSEventOwnerHasDestroy) {
        presetStoreValueEventChanged =
            parent.register(preset.store.value.eventChanged.listen {
            onStoreChange()
        })
    }

    private fun onStoreChange() {
        val newValue = load()
        if (_value == newValue) return
        _value = newValue
        onApply?.invoke(newValue)
        eventChange.fire(newValue)
    }

    override fun value(newValue: T, fire: Boolean) {
        if (_value == newValue) return
        _value = newValue
        presetStoreValueEventChanged.pause().use { saveTo(preset.store.value) }
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }

    override var value: T
        get() = _value
        set(value) = value(value)

    override fun toString() = "$key $value"
}