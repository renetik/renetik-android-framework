package renetik.android.framework.preset.property.value

import renetik.android.framework.protocol.CSEventOwnerHasDestroy
import renetik.android.core.lang.CSHasId
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStore
import renetik.android.core.kotlin.toId

class CSListValuePresetEventProperty<T : CSHasId>(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>, key: String, val values: Iterable<T>,
    override val default: List<T>, onChange: ((value: List<T>) -> Unit)?)
    : CSValuePresetEventProperty<List<T>>(parent, preset, key, onChange) {

    override var _value = load()

    override fun get(store: CSStore) = store.get(key)?.split(",")
        ?.mapNotNull { categoryId -> values.find { it.id == categoryId } } ?: default

    override fun set(store: CSStore, value: List<T>) =
        store.set(key, value.joinToString(",") { it.toId() })
}