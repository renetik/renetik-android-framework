package renetik.android.client.request

import renetik.android.java.extensions.collections.isEmpty
import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.collections.put
import renetik.android.java.extensions.collections.putAll

open class CSConcurrentProcess<T : Any>(data: MutableList<T>) : CSProcess<List<T>>(data) {

    private val processes: MutableList<CSProcess<T>> = list()
    private val runningProcesses: MutableList<CSProcess<T>> = list()

    constructor() : this(list())

    constructor(vararg adding: CSProcess<T>) : this() {
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