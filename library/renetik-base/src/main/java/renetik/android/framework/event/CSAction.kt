package renetik.android.framework.event

import renetik.android.framework.base.CSApplication.Companion.app
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.lang.property.isTrue
import renetik.android.framework.lang.property.setFalse
import renetik.android.framework.lang.property.setTrue
import renetik.android.framework.store.property.value.CSBooleanValueStoreEventProperty

typealias CSActionInterface = CSEventProperty<Boolean>

fun CSActionInterface.start() = setTrue()
fun CSActionInterface.stop() = setFalse()
val CSActionInterface.isRunning get() = isTrue

class CSAction {
    companion object {

        var actionsDisabled = false

        fun action(id: String, function: ((Boolean) -> Unit)? = null): CSActionInterface =
            object : CSBooleanValueStoreEventProperty(app.store, id, false, function) {
                override var _value = if (!actionsDisabled) load() else false
            }
    }
}