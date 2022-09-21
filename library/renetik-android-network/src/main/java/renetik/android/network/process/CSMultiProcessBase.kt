package renetik.android.network.process

import renetik.android.core.lang.ArgFunc
import renetik.android.event.common.CSHasDestroy

open class CSMultiProcessBase<Data : Any>(
    parent: CSHasDestroy, data: Data? = null) : CSProcessBase<Data>(parent, data) {

    protected var addedProcess: CSProcessBase<*>? = null

    fun addLast(process: CSProcessBase<Data>): CSProcessBase<Data> {
        process.onSuccess { success(it.data!!) }
        return add(process)
    }

    fun <V : Any> add(process: CSProcessBase<V>, isLast: Boolean): CSProcessBase<V> {
        if (isLast) process.onSuccess { success() }
        return add(process)
    }

    fun <V : Any> add(process: CSProcessBase<V>,
                      onSuccess: ArgFunc<CSProcessBase<V>>? = null): CSProcessBase<V> {
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
