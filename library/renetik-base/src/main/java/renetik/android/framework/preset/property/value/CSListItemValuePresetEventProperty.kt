package renetik.android.framework.preset.property.value

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.lang.property.CSListValuesEventProperty
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.getValue
import renetik.kotlin.toId

open class CSListItemValuePresetEventProperty<T>(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    val getValues: () -> Collection<T>,
    val getDefault: () -> T,
    onChange: ((value: T) -> Unit)? = null,
) : CSValuePresetEventProperty<T>(parent, preset, key, onChange), CSListValuesEventProperty<T> {

    constructor(
        parent: CSEventOwnerHasDestroy,
        preset: CSPreset<*, *>, key: String,
        values: Collection<T>, default: T, onChange: ((value: T) -> Unit)? = null,
    ) : this(parent, preset, key, getValues = { values },
        getDefault = { default }, onChange)

    override val values: List<T> get() = getValues().toList()
    override val default: T get() = getDefault()
    override var _value: T = load()
    override fun get(store: CSStoreInterface): T? = store.getValue(key, values)
    override fun set(store: CSStoreInterface, value: T) = store.set(key, value.toId())
}