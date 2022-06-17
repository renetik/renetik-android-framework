package renetik.android.framework.event

import renetik.android.core.lang.property.isTrue
import renetik.android.core.lang.property.setFalse
import renetik.android.core.lang.property.setTrue
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.store.CSStore.Companion.store
import renetik.android.framework.store.property.value.CSBooleanValueStoreEventProperty

typealias CSActionInterface = CSEventProperty<Boolean>

fun CSActionInterface.start() = setTrue()
fun CSActionInterface.stop() = setFalse()
val CSActionInterface.isRunning get() = isTrue

class CSAction {
    companion object {

        var actionsDisabled = false

        fun action(id: String, function: ((Boolean) -> Unit)? = null): CSActionInterface =
            object : CSBooleanValueStoreEventProperty(store, id, false, function) {
                override var _value = if (!actionsDisabled) load() else false
            }
    }
}