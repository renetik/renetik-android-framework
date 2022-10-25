package renetik.android.network.process

import renetik.android.core.kotlin.collections.isEmpty
import renetik.android.core.kotlin.collections.list
import renetik.android.core.kotlin.collections.put
import renetik.android.core.kotlin.collections.putAll
import renetik.android.event.common.CSHasDestruct

open class CSConcurrentProcess<T : Any>(
    parent: CSHasDestruct,
    data: MutableList<T>) : CSProcess<List<T>>(parent, data) {
    private val processes: MutableList<CSProcess<T>> = list()
    private val runningProcesses: MutableList<CSProcess<T>> = list()

    constructor(parent: CSHasDestruct) : this(parent, list())

    constructor(parent: CSHasDestruct, vararg adding: CSProcess<T>) : this(parent) {
        runningProcesses.putAll(processes.putAll(*adding)).forEach { response ->
            response.onSuccess { onResponseSuccess(it) }
            response.onFailed { onResponseFailed(it) }
        }
    }

    fun add(process: CSProcess<T>) =
        runningProcesses.put(processes.put(process))
            .onSuccess { onResponseSuccess(it) }.onFailed { onResponseFailed(it) }

    private fun onResponseSuccess(succeededProcess: CSProcess<*>) {
        runningProcesses.remove(succeededProcess)
        if (runningProcesses.isEmpty) {
            val mutableListData = (data as MutableList)
            processes.forEach { response -> mutableListData.add(response.data!!) }
            success(mutableListData)
        }
    }

    private fun onResponseFailed(failedProcess: CSProcess<*>) {
        runningProcesses.apply { remove(failedProcess) }.forEach { response -> response.cancel() }
        failed(failedProcess)
    }

    override fun cancel() {
        runningProcesses.forEach { it.cancel() }
        super.cancel()
    }
}