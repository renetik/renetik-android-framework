package renetik.android.network.process

class CSProcess<Data : Any>(function: CSProcess<Data>.() -> Unit) :
    CSProcessBase<Data>() {
    init {
        later { function() }
    }
}

