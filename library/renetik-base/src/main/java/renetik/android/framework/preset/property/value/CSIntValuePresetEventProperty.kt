package renetik.android.framework.preset.property.value

import renetik.android.framework.protocol.CSEventOwnerHasDestroy
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStore

class CSIntValuePresetEventProperty(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    override val default: Int,
    onChange: ((value: Int) -> Unit)?)
    : CSValuePresetEventProperty<Int>(parent,preset, key, onChange) {
    override var _value = load()
    override fun get(store: CSStore) = store.getInt(key)
    override fun set(store: CSStore, value: Int) = store.set(key, value)
}


