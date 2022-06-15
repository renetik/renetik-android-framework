package renetik.android.framework.preset.property.value

import renetik.android.framework.protocol.CSEventOwnerHasDestroy
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStore

class CSStringValuePresetEventProperty(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    override val default: String,
    onChange: ((value: String) -> Unit)?)
    : CSValuePresetEventProperty<String>(parent, preset, key, onChange) {
    override var _value = load()
    override fun get(store: CSStore) = store.getString(key)
    override fun set(store: CSStore, value: String) = store.set(key, value)
}

