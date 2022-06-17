package renetik.android.framework.preset.property.value

import renetik.android.event.owner.CSEventOwnerHasDestroy
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStore

class CSBooleanValuePresetEventProperty(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    override val default: Boolean,
    onChange: ((value: Boolean) -> Unit)?)
    : CSValuePresetEventProperty<Boolean>(parent,preset, key, onChange) {
    override var _value = load()
    override fun get(store: CSStore) = store.getBoolean(key)
    override fun set(store: CSStore, value: Boolean) = store.set(key, value)
}