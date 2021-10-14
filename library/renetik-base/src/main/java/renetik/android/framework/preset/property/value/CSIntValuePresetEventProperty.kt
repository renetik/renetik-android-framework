package renetik.android.framework.preset.property.value

import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface

class CSIntValuePresetEventProperty(
    preset: CSPreset<*, *>,
    key: String,
    override val default: Int,
    onChange: ((value: Int) -> Unit)?)
    : CSValuePresetEventProperty<Int>(preset, key, onChange) {
    override var _value = load()
    override fun get(store: CSStoreInterface) = store.getInt(key)
    override fun set(store: CSStoreInterface, value: Int) = store.set(key, value)
}


