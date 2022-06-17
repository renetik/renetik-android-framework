package renetik.android.framework.store.property

import renetik.android.framework.store.CSStore
import renetik.android.framework.store.property.value.CSBooleanValueStoreEventProperty

class CSAction {
    companion object {
        var actionsDisabled = false
        fun action(id: String, function: ((Boolean) -> Unit)? = null): CSActionInterface =
            object : CSBooleanValueStoreEventProperty(CSStore.store, id, false, function) {
                override var _value = if (!actionsDisabled) load() else false
            }
    }
}