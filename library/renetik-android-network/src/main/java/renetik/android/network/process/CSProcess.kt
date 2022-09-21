package renetik.android.network.process

import renetik.android.event.common.CSHasDestroy
import renetik.android.event.registration.later

class CSProcess<Data : Any>(
    parent: CSHasDestroy,
    function: CSProcess<Data>.() -> Unit) : CSProcessBase<Data>(parent) {
    init {
        later { function() }
    }
}

