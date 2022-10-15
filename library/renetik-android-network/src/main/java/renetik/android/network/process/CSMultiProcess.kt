package renetik.android.network.process

import renetik.android.event.common.CSHasDestruct
import renetik.android.event.registration.later

class CSMultiProcess<Data : Any>(
    parent: CSHasDestruct, function: CSMultiProcess<Data>.() -> Unit) :
    CSMultiProcessBase<Data>(parent) {
    init {
        later { function() }
    }
}