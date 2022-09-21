package renetik.android.network.process

import renetik.android.event.common.CSHasDestroy
import renetik.android.event.registration.later

class CSMultiProcess<Data : Any>(
    parent: CSHasDestroy, function: CSMultiProcess<Data>.() -> Unit) :
    CSMultiProcessBase<Data>(parent) {
    init {
        later { function() }
    }
}