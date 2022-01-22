package renetik.android.framework.preset.property.nullable

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface

class CSStringNullablePresetEventProperty(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    override val default: String?,
    onChange: ((value: String?) -> Unit)?)
    : CSNullablePresetEventProperty<String>(parent, preset, key, onChange) {
    override var _value = load()
    override fun get(store: CSStoreInterface): String? = store.getString(key)
    override fun set(store: CSStoreInterface, value: String?) {
        store.set(key, value)
    }
}