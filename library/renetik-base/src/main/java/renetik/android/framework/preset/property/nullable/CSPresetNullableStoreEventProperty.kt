package renetik.android.framework.preset.property.nullable

import renetik.android.framework.preset.CSPreset
import renetik.android.framework.preset.property.CSPresetStoreEventProperty
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.nullable.CSNullableStoreEventProperty

abstract class CSPresetNullableStoreEventProperty<T>(
    final override val preset: CSPreset<*, *>,
    override val key: String,
    val getDefault: () -> T?) : CSPresetStoreEventProperty<T?> {

    constructor(preset: CSPreset<*, *>, key: String, default: T?)
            : this(preset, key, { default })

    abstract val property: CSNullableStoreEventProperty<T>
    override val store get() = preset.store.value
    private fun load(store: CSStoreInterface) = property.load(store) ?: getDefault()
    override fun reload() = value(load(store))
    override fun isModified(): Boolean = value != load(preset.current.value.store)
    override fun save(store: CSStoreInterface, value: T?) = property.save(store, value)
    override fun value(newValue: T?, fire: Boolean) =
        property.value(newValue, fire)

    override fun onChanged(function: (before: T?, after: T?) -> Unit) =
        property.onChanged(function)

    override var value
        get() = property.value
        set(value) = property.value(value)

    init {
        preset.store.onChange {
            property.store = it
            reload()
        }
//        preset.eventReload.listen(::reload)
    }
}