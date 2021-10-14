package renetik.android.framework.preset

import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.event.listen
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.logging.CSLog.info
import renetik.android.framework.preset.property.value.CSJsonTypeValuePresetEventProperty

class CSStoreJsonTypePresetValueStoreEventProperty(
    preset: CSPreset<*, *>,
    parentPreset: CSPreset<*, *>,
    onChange: ((value: CSJsonObject) -> Unit)? = null)
    : CSJsonTypeValuePresetEventProperty<CSJsonObject>(
    parentPreset, "${preset.id} store", CSJsonObject::class, onChange) {

    override fun isModified() = preset.current.value.store.has(key)
            && value != get(preset.current.value.store)

    init {
        registerValueDataChange()
    }

    override fun onValueChanged(newValue: CSJsonObject, fire: Boolean, before: CSJsonObject) {
        super.onValueChanged(newValue, fire, before)
        registerValueDataChange()
    }

    //    TODO needed ?
    var storeDataChangeRegistration: CSEventRegistration? = null
    private fun registerValueDataChange() {
        storeDataChangeRegistration?.cancel()
        storeDataChangeRegistration = value.eventChanged.listen { data ->
//            info("")
//            save(store)
//            onApply?.invoke(value)
//            fireChange(value, value) //TODO Wrong...
        }
    }
}