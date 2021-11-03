package renetik.android.framework.preset

import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.preset.property.value.CSJsonTypeValuePresetEventProperty

class CSStoreJsonTypePresetValueStoreEventProperty(
    preset: CSPreset<*, *>,
    parentPreset: CSPreset<*, *>,
    onChange: ((value: CSJsonObject) -> Unit)? = null)
    : CSJsonTypeValuePresetEventProperty<CSJsonObject>(
    preset, parentPreset, "${preset.id} store", CSJsonObject::class, onChange) {

}