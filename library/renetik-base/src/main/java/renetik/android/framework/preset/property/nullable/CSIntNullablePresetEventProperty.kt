package renetik.android.framework.preset.property.nullable

import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.nullable.CSIntNullableStoreEventProperty

class CSIntNullablePresetEventProperty(
    preset: CSPreset<*, *>,
    key: String,
    override val default: Int?,
    onChange: ((value: Int?) -> Unit)?)
    : CSNullablePresetEventProperty<Int>(preset, key,onChange) {
    override fun get(store: CSStoreInterface): Int? = store.getInt(key)
    override fun set(store: CSStoreInterface, value: Int?) {
        if (value == null) store.clear(key) else store.set(key, value)
    }
}


