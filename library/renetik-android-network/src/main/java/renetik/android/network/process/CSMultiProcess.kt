package renetik.android.network.process

class CSMultiProcess<Data : Any>(function: CSMultiProcess<Data>.() -> Unit) :
    CSMultiProcessBase<Data>() {
    init {
        later { function() }
    }
}