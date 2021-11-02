package renetik.android.framework.preset.property.nullable

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface

class CSFloatNullablePresetEventProperty(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    override val default: Float?,
    onChange: ((value: Float?) -> Unit)?)
    : CSNullablePresetEventProperty<Float>(parent, preset, key, onChange) {
    override var _value = load()
    override fun get(store: CSStoreInterface): Float? = store.getFloat(key)
    override fun set(store: CSStoreInterface, value: Float?) {
        store.set(key, value)
    }
}