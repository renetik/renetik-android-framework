package renetik.android.preset.property.nullable

import renetik.android.event.owner.CSEventOwnerHasDestroy
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

class CSStringNullablePresetEventProperty(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    override val default: String?,
    onChange: ((value: String?) -> Unit)?)
    : CSNullablePresetEventProperty<String>(parent, preset, key, onChange) {
    override var _value = load()
    override fun get(store: CSStore): String? = store.getString(key)
    override fun set(store: CSStore, value: String?) {
        store.set(key, value)
    }
}