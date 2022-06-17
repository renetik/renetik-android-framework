package renetik.android.preset.property.value

import renetik.android.event.owner.CSEventOwnerHasDestroy
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

class CSDoubleValuePresetEventProperty(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    override val default: Double,
    onChange: ((value: Double) -> Unit)?)
    : CSValuePresetEventProperty<Double>(parent,preset, key, onChange) {
    override var _value = load()
    override fun get(store: CSStore) = store.getDouble(key)
    override fun set(store: CSStore, value: Double) = store.set(key, value)
}