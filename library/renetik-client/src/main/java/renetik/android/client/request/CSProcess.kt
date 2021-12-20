package renetik.android.client.request

import renetik.android.framework.task.CSDoLaterObject.later

class CSProcess<Data : Any>(function: CSProcess<Data>.() -> Unit) :
    CSProcessBase<Data>() {
    init {
        later { function() }
    }
}

class CSMultiProcess<Data : Any>(function: CSMultiProcess<Data>.() -> Unit) :
    CSMultiProcessBase<Data>() {
    init {
        later { function() }
    }
}