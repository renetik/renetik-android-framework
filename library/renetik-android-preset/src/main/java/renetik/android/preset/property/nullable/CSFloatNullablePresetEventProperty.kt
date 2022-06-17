package renetik.android.preset.property.nullable

import renetik.android.event.owner.CSEventOwnerHasDestroy
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

class CSFloatNullablePresetEventProperty(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    override val default: Float?,
    onChange: ((value: Float?) -> Unit)?)
    : CSNullablePresetEventProperty<Float>(parent, preset, key, onChange) {
    override var _value = load()
    override fun get(store: CSStore): Float? = store.getFloat(key)
    override fun set(store: CSStore, value: Float?) {
        store.set(key, value)
    }
}