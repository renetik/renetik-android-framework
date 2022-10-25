package renetik.android.network.process

import renetik.android.core.lang.ArgFunc
import renetik.android.event.common.CSHasDestruct

open class CSMultiProcessBase<Data : Any>(
    parent: CSHasDestruct, data: Data? = null) : CSProcess<Data>(parent, data) {

    protected var addedProcess: CSProcess<*>? = null

    fun addLast(process: CSProcess<Data>): CSProcess<Data> {
        process.onSuccess { success(it.data!!) }
        return add(process)
    }

    fun <V : Any> add(process: CSProcess<V>, isLast: Boolean): CSProcess<V> {
        if (isLast) process.onSuccess { success() }
        return add(process)
    }

    fun <V : Any> add(process: CSProcess<V>,
                      onSuccess: ArgFunc<CSProcess<V>>? = null): CSProcess<V> {
        addedProcess = process
        process.onFailed { failed(it) }
        onSuccess?.let { process.onSuccess(it) }
        return process
    }

    override fun cancel() {
        super.cancel()
        addedProcess?.cancel()
    }

}
