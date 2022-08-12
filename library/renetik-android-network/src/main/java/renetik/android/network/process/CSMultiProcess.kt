package renetik.android.network.process

import renetik.android.event.registration.later

class CSMultiProcess<Data : Any>(function: CSMultiProcess<Data>.() -> Unit) :
    CSMultiProcessBase<Data>() {
    init {
        later { function() }
    }
}