package renetik.android.framework.preset

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

//    init {
//        registerValueDataChange()
//    }
//
//    override var _value = load()
//    set(value) {
//        field = value
//        registerValueDataChange()
//    }

//    override fun onValueChanged(newValue: CSJsonObject, fire: Boolean, before: CSJsonObject) {
//        super.onValueChanged(newValue, fire, before)
//        registerValueDataChange()
//    }

//    //    TODO needed ?
//    var storeDataChangeRegistration: CSEventRegistration? = null
//    private fun registerValueDataChange() {
//        storeDataChangeRegistration?.cancel()
//        storeDataChangeRegistration = value.eventChanged.listen { data ->
////            info("")
////            save(store)
////            onApply?.invoke(value)
////            fireChange(value, value) //TODO Wrong...
//        }

//    override fun value(newValue: CSJsonObject, fire: Boolean) {
//        if (_value == newValue) return
//        _value.reload(newValue)
//        save()
//        updateIsModified()
//        onApply?.invoke(newValue)
//        if (fire) eventChange.fire(newValue)
//    }

}