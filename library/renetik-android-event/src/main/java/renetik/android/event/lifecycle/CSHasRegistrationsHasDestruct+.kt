package renetik.android.event.lifecycle

import renetik.android.event.change.invoke
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.plus

fun <T> T.parent(parent: CSHasDestruct)
        where  T : CSHasRegistrations, T : CSHasDestruct = apply {
    this + parent.eventDestruct { if (!isDestructed) destruct() }
}
