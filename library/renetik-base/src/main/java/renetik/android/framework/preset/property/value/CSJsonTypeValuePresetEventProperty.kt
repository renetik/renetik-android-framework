package renetik.android.framework.preset.property.value

import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.event.listen
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface
import renetik.kotlin.reflect.createInstance
import kotlin.reflect.KClass

open class CSJsonTypeValuePresetEventProperty<T : CSJsonObject>(
    preset: CSPreset<*, *>, key: String, val type: KClass<T>,
    onChange: ((value: T) -> Unit)? = null)
    : CSValuePresetEventProperty<T>(preset, key, onChange) {

    override val default get() = type.createInstance()!!
    override var _value = load()
    override fun get(store: CSStoreInterface) = store.getJsonObject(key, type)
    override fun set(store: CSStoreInterface, value: T) = store.set(key, value)

//    init {
//        registerValueDataChange()
//    }

//    override fun onValueChanged(newValue: T, fire: Boolean, before: T) {
//        super.onValueChanged(newValue, fire, before)
//        registerValueDataChange()
//    }

//    TODO needed ?
//    var storeDataChangeRegistration: CSEventRegistration? = null
//    private fun registerValueDataChange() {
//        storeDataChangeRegistration?.cancel()
//        storeDataChangeRegistration = value.eventChanged.listen { data ->
//            save(store)
//            onApply?.invoke(value)
//            fireChange(value, value) //TODO Wrong...
//        }
//    }
}


