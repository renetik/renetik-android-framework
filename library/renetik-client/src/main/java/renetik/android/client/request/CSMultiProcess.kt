package renetik.android.client.request

open class CSMultiProcess<Data : Any>(data: Data? = null) : CSProcessBase<Data>(data) {

    protected var addedProcess: CSProcessBase<*>? = null

    fun addLast(process: CSProcessBase<Data>): CSProcessBase<Data> {
        process.onSuccess { success(it.data!!) }
        return add(process)
    }

    fun <V : Any> add(process: CSProcessBase<V>, isLast: Boolean): CSProcessBase<V> {
        if (isLast) process.onSuccess { success() }
        return add(process)
    }

    fun <V : Any> add(process: CSProcessBase<V>): CSProcessBase<V> {
        addedProcess = process
        process.onFailed { failed(it) }
        return process
    }

    override fun cancel() {
        super.cancel()
        addedProcess?.cancel()
    }

}
