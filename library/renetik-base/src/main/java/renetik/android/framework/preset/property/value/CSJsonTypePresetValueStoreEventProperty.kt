package renetik.android.framework.preset.property.value

import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.property.value.CSJsonTypeValueStoreEventProperty
import kotlin.reflect.KClass

class CSJsonTypePresetValueStoreEventProperty<T : CSJsonObject>(
    preset: CSPreset<*, *>, key: String, type: KClass<T>,
    onChange: ((value: T) -> Unit)? = null)
    : CSPresetValueStoreEventProperty<T>(preset, key) {
    override val property = CSJsonTypeValueStoreEventProperty(store, key, type, onChange)
}