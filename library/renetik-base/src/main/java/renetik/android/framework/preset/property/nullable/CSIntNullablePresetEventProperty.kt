package renetik.android.framework.preset.property.nullable

import renetik.android.framework.base.CSEventOwnerHasDestroy
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStore

class CSIntNullablePresetEventProperty(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    override val default: Int?,
    onChange: ((value: Int?) -> Unit)?)
    : CSNullablePresetEventProperty<Int>(parent, preset, key, onChange) {
    override var _value = load()
    override fun get(store: CSStore): Int? = store.getInt(key)
    override fun set(store: CSStore, value: Int?) {
        store.set(key, value)
    }
}


