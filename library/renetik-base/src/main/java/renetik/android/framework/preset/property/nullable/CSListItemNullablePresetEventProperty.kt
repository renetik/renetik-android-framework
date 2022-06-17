package renetik.android.framework.preset.property.nullable

import renetik.android.framework.protocol.CSEventOwnerHasDestroy
import renetik.android.framework.event.property.CSListValuesEventProperty
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStore
import renetik.android.framework.store.getValue
import renetik.android.core.kotlin.toId

class CSListItemNullablePresetEventProperty<T>(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    val getValues: () -> List<T>,
    val getDefault: () -> T,
    onChange: ((value: T?) -> Unit)? = null)
    : CSNullablePresetEventProperty<T>(parent, preset, key, onChange),
    CSListValuesEventProperty<T?> {

    constructor(
        parent: CSEventOwnerHasDestroy,
        preset: CSPreset<*, *>, key: String,
        values: List<T>, default: T, onChange: ((value: T?) -> Unit)? = null
    ) : this(parent, preset, key, getValues = { values },
        getDefault = { default }, onChange)

    override val values: List<T> get() = getValues()
    override val default: T get() = getDefault()
    override var _value = load()
    override fun get(store: CSStore) = store.getValue(key, values)
    override fun set(store: CSStore, value: T?) {
        store.set(key, value.toId())
    }
}