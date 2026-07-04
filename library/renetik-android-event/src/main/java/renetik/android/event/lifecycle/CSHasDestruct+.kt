package renetik.android.event.lifecycle

import renetik.android.core.lang.Fun
import renetik.android.event.change.CSHasChangeValue
import renetik.android.event.change.invoke
import renetik.android.event.delegate
import renetik.android.event.listen
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.plus

fun <T : CSHasDestruct> T.destruct() = onDestruct()

inline fun CSHasDestruct.onDestructed(crossinline func: Fun){
    eventDestruct.listen(func)
}

inline fun CSHasDestruct.onDestructed(
    parent: CSHasRegistrations, crossinline func: Fun
) {
    var registration: CSRegistration? = null
    registration = parent + eventDestruct.invoke {
        registration?.cancel()
        func()
    }
}

val CSHasDestruct.isDestruct: CSHasChangeValue<Boolean>
    get() = eventDestruct.delegate { isDestructed }