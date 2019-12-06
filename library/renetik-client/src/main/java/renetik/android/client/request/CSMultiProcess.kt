package renetik.android.client.request

open class CSMultiProcess<Data : Any>(data: Data? = null) : CSProcess<Data>(data) {

    protected var addedProcess: CSProcess<*>? = null

    fun addLast(process: CSProcess<Data>): CSProcess<Data> {
        process.onSuccess { success(it.data!!) }
        return add(process)
    }

    fun <V : Any> add(process: CSProcess<V>, isLast: Boolean): CSProcess<V> {
        if (isLast) process.onSuccess { success() }
        return add(process)
    }

    fun <V : Any> add(process: CSProcess<V>): CSProcess<V> {
        addedProcess = process
        process.onFailed { failed(it) }
        return process
    }

    override fun cancel() {
        super.cancel()
        addedProcess?.cancel()
    }

}
