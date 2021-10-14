package renetik.android.framework.preset.property.nullable

import renetik.android.framework.lang.property.CSListValuesProperty
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.getValue
import renetik.kotlin.toId

class CSListItemNullablePresetEventProperty<T>(
    preset: CSPreset<*, *>,
    key: String,
    override val values: List<T>,
    override val default: T?,
    onChange: ((value: T?) -> Unit)?)
    : CSNullablePresetEventProperty<T>(preset, key, onChange), CSListValuesProperty<T?> {
    override fun get(store: CSStoreInterface) = store.getValue(key, values)
    override fun set(store: CSStoreInterface, value: T?) {
        if (value == null) store.clear(key) else store.set(key, value.toId())
    }
}