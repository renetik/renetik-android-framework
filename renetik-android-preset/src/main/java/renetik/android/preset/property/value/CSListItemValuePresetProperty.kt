package renetik.android.preset.property.value

import renetik.android.core.kotlin.toId
import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.event.property.CSListValuesProperty
import renetik.android.json.obj.getValue
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

open class CSListItemValuePresetProperty<T>(
    parent: CSHasDestruct, preset: CSPreset<*, *>,
    key: String, val getValues: () -> Collection<T>, val getDefault: () -> T,
    onChange: ((value: T) -> Unit)? = null
) : CSValuePresetProperty<T>(parent, preset, key, onChange), CSListValuesProperty<T> {

    constructor(
        parent: CSHasDestruct,
        preset: CSPreset<*, *>, key: String,
        values: Collection<T>, default: T, onChange: ((value: T) -> Unit)? = null,
    ) : this(parent, preset, key, getValues = { values },
        getDefault = { default }, onChange
    )

    override val values: List<T> get() = getValues().toList()
    override val default: T get() = getDefault()
    override fun get(store: CSStore): T? = store.getValue(key, values)
    override fun set(store: CSStore, value: T) = store.set(key, value.toId())
}