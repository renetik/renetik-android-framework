package renetik.android.framework.event

import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.event.property.CSEventPropertyFunctions.property
import renetik.android.framework.lang.property.isTrue
import renetik.android.framework.lang.property.setFalse
import renetik.android.framework.lang.property.setTrue

typealias CSActionInterface = CSEventProperty<Boolean>

fun CSActionInterface.start() = setTrue()
fun CSActionInterface.stop() = setFalse()
val CSActionInterface.isRunning get() = isTrue

class CSAction {
    companion object {
        fun action(id: String, function: ((Boolean) -> Unit)? = null):
                CSActionInterface = property(id, false, function)
    }
}