package renetik.android.network.process

import renetik.android.event.common.CSHasDestruct
import renetik.android.event.registration.later

class CSProcess<Data : Any>(
    parent: CSHasDestruct,
    function: CSProcess<Data>.() -> Unit) : CSProcessBase<Data>(parent) {
    init {
        later { function() }
    }
}

