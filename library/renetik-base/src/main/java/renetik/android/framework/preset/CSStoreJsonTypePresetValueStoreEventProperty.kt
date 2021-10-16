package renetik.android.framework.preset

import renetik.android.framework.event.pause
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.preset.property.value.CSJsonTypeValuePresetEventProperty

class CSStoreJsonTypePresetValueStoreEventProperty(
    preset: CSPreset<*, *>,
    parentPreset: CSPreset<*, *>,
    onChange: ((value: CSJsonObject) -> Unit)? = null)
    : CSJsonTypeValuePresetEventProperty<CSJsonObject>(
    preset, parentPreset, "${preset.id} store", CSJsonObject::class, onChange) {

    override fun updateIsModified() {
        val itemStore = preset.item.value.store
        isModified.value(itemStore.has(key) && value != get(itemStore))
    }

//    override fun value(newValue: CSJsonObject, fire: Boolean) {
//        if (_value == newValue) return
//        _value.reload(newValue)
//        save()
//        updateIsModified()
//        onApply?.invoke(newValue)
//        if (fire) eventChange.fire(newValue)
//    }

}