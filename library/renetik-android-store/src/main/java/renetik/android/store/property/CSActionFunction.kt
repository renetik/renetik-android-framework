package renetik.android.store.property

import renetik.android.event.property.CSActionInterface
import renetik.android.event.property.CSEventPropertyFunctions.property

class CSActionFunction {
    companion object {
        var actionsDisabled = false
//        fun action(id: String, function: ((Boolean) -> Unit)? = null): CSActionInterface =
//            object : CSBooleanValueStoreEventProperty(app.store, id, false, function) {
//                override var _value = if (!actionsDisabled) load() else false
//            }

        fun action(id: String, function: ((Boolean) -> Unit)? = null): CSActionInterface =
            property(false, function)
    }
}