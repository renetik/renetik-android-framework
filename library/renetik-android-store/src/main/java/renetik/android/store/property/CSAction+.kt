package renetik.android.store.property

import renetik.android.core.lang.property.isTrue
import renetik.android.core.lang.property.setFalse
import renetik.android.core.lang.property.setTrue
import renetik.android.event.property.CSEventProperty

typealias CSActionInterface = CSEventProperty<Boolean>
fun CSActionInterface.start() = setTrue()
fun CSActionInterface.stop() = setFalse()
val CSActionInterface.isRunning get() = isTrue