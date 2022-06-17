package renetik.android.event.property

import renetik.android.core.lang.property.isTrue
import renetik.android.core.lang.property.setFalse
import renetik.android.core.lang.property.setTrue

typealias CSActionInterface = CSEventProperty<Boolean>
fun CSActionInterface.start() = setTrue()
fun CSActionInterface.stop() = setFalse()
val CSActionInterface.isRunning get() = isTrue