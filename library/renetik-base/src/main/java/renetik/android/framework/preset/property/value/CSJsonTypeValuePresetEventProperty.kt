package renetik.android.framework.preset.property.value

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface
import renetik.kotlin.reflect.createInstance
import kotlin.reflect.KClass

open class CSJsonTypeValuePresetEventProperty<T : CSJsonObject>(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>, key: String, val type: KClass<T>,
    onChange: ((value: T) -> Unit)? = null)
    : CSValuePresetEventProperty<T>(parent, preset, key, onChange) {

    override val default get() = type.createInstance()!!
    override var _value = load()
    override fun get(store: CSStoreInterface) = store.getJsonObject(key, type)
    override fun set(store: CSStoreInterface, value: T) = store.set(key, value)

//    var valueEventChangedRegistration: CSEventRegistration

//        set(value) {
//            field = value
//            registerValueDataChange()
//            valueEventChangedRegistration.cancel()
//            valueEventChangedRegistration = value.eventChanged.listen { save() }
//        }


//    init {
//        valueEventChangedRegistration = value.eventChanged.listen { save() }
//    }

//    fun save() {
//        save(preset.store.value)
//    }


//    init {
//        registerValueDataChange()
//    }

//    override fun onValueChanged(newValue: T, fire: Boolean, before: T) {
//        super.onValueChanged(newValue, fire, before)
//        registerValueDataChange()
//    }

//    var storeDataChangeRegistration: CSEventRegistration? = null
//    private fun registerValueDataChange() {
//        storeDataChangeRegistration?.cancel()
//        storeDataChangeRegistration = value.eventChanged.listen { data ->
//            save(preset.store.value)
//            apply()
//            onApply?.invoke(value)
//            fireChange(value, value) //TODO Wrong...
//        }
//    }
}


