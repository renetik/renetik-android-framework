package renetik.android.framework.preset.property.value

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.preset.property.CSPresetEventProperty
import renetik.android.framework.preset.property.CSPresetEventPropertyBase
import renetik.android.framework.store.CSStoreInterface

abstract class CSValuePresetEventProperty<T>(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    override val key: String,
    onChange: ((value: T) -> Unit)? = null
) : CSPresetEventPropertyBase<T>(parent, preset, key, onChange), CSPresetEventProperty<T> {

    override fun loadFrom(store: CSStoreInterface): T =
        get(store) ?: default.also { set(store, it) }
}