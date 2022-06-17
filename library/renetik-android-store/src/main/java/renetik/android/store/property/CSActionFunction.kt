package renetik.android.store.property

import renetik.android.event.property.CSActionInterface
import renetik.android.store.CSStore
import renetik.android.store.property.value.CSBooleanValueStoreEventProperty

class CSActionFunction {
    companion object {
        var actionsDisabled = false
        fun action(id: String, function: ((Boolean) -> Unit)? = null): CSActionInterface =
            object : CSBooleanValueStoreEventProperty(CSStore.store, id, false, function) {
                override var _value = if (!actionsDisabled) load() else false
            }
    }
}