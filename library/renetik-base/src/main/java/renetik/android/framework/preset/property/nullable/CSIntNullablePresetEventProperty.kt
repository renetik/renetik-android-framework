package renetik.android.framework.preset.property.nullable

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface

class CSIntNullablePresetEventProperty(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    override val default: Int?,
    onChange: ((value: Int?) -> Unit)?)
    : CSNullablePresetEventProperty<Int>(parent, preset, key, onChange) {
    //    override var _value: Int? = load()
    override var _value = load()
    override fun get(store: CSStoreInterface): Int? = store.getInt(key)
    override fun set(store: CSStoreInterface, value: Int?) {
        store.set(key, value)
    }
}


