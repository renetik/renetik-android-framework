package renetik.android.network.process

import renetik.android.event.registration.later

class CSProcess<Data : Any>(function: CSProcess<Data>.() -> Unit) :
    CSProcessBase<Data>() {
    init {
        later { function() }
    }
}

