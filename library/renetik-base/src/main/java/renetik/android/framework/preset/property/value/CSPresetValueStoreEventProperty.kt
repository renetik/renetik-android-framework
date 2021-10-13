package renetik.android.framework.preset.property.value

import renetik.android.framework.preset.CSPreset
import renetik.android.framework.preset.property.CSPresetStoreEventProperty
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.value.CSValueStoreEventProperty

abstract class CSPresetValueStoreEventProperty<T>(
    final override val preset: CSPreset<*, *>,
    override val key: String,
) : CSPresetStoreEventProperty<T> {

    abstract val property: CSValueStoreEventProperty<T>
    override val store get() = preset.store.value
    override fun isModified(): Boolean {
        val itemStore = preset.current.value.store
        if (key.endsWith("preset store")) {
            return itemStore.has(key) &&
                    value != (property.get(itemStore) ?: property.defaultValue)
        }
        return value != (property.get(itemStore) ?: property.defaultValue)
    }

    init {
        preset.store.save()
        preset.store.onChange {
            property.store = it
        }
    }

    override fun set(store: CSStoreInterface, value: T) = property.set(store, value)
    override fun value(newValue: T, fire: Boolean) =
        property.value(newValue, fire)

    override fun onChanged(function: (before: T, after: T) -> Unit) =
        property.onChanged { before: T, after: T ->
            function(before, after)
            preset.store.save()
        }

    override var value
        get() = property.value
        set(value) = property.value(value)

    @Deprecated("")
    fun apply() = apply { property.apply() }
}