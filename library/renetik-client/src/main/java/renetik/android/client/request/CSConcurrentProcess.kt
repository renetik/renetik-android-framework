package renetik.android.client.request

import renetik.android.core.kotlin.collections.isEmpty
import renetik.android.core.kotlin.collections.list
import renetik.android.core.kotlin.collections.put
import renetik.android.core.kotlin.collections.putAll

open class CSConcurrentProcess<T : Any>(data: MutableList<T>) : CSProcessBase<List<T>>(data) {

    private val processes: MutableList<CSProcessBase<T>> = list()
    private val runningProcesses: MutableList<CSProcessBase<T>> = list()

    constructor() : this(list())

    constructor(vararg adding: CSProcessBase<T>) : this() {
        runningProcesses.putAll(processes.putAll(*adding)).forEach { response ->
            response.onSuccess { onResponseSuccess(it) }
            response.onFailed { onResponseFailed(it) }
        }
    }

    fun add(process: CSProcessBase<T>) =
        runningProcesses.put(processes.put(process))
            .onSuccess { onResponseSuccess(it) }.onFailed { onResponseFailed(it) }

    private fun onResponseSuccess(succeededProcess: CSProcessBase<*>) {
        runningProcesses.remove(succeededProcess)
        if (runningProcesses.isEmpty) {
            val mutableListData = (data as MutableList)
            processes.forEach { response -> mutableListData.add(response.data!!) }
            success(mutableListData)
        }
    }

    private fun onResponseFailed(failedProcess: CSProcessBase<*>) {
        runningProcesses.apply { remove(failedProcess) }.forEach { response -> response.cancel() }
        failed(failedProcess)
    }

    override fun cancel() {
        runningProcesses.forEach { it.cancel() }
        super.cancel()
    }
}