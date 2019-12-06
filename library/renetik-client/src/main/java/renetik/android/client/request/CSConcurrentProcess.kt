package renetik.android.client.request

import renetik.android.java.extensions.collections.isEmpty
import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.collections.put
import renetik.android.java.extensions.collections.putAll

open class CSConcurrentProcess(data: MutableList<Any>) : CSProcess<List<Any>>(data) {

    private val processes: MutableList<CSProcess<*>> = list()
    private val runningProcesses: MutableList<CSProcess<*>> = list()

    constructor() : this(list())

    constructor(vararg adding: CSProcess<*>) : this() {
        runningProcesses.putAll(processes.putAll(*adding)).forEach { response ->
            response.onSuccess { onResponseSuccess(it) }
            response.onFailed { onResponseFailed(it) }
        }
    }

    fun add(process: CSProcess<*>) =
            runningProcesses.put(processes.put(process))
                    .onSuccess { onResponseSuccess(it) }.onFailed { onResponseFailed(it) }

    private fun onResponseSuccess(succeededProcess: CSProcess<*>) {
        if (runningProcesses.apply { remove(succeededProcess) }.isEmpty)
            success((data as MutableList).apply {
                processes.forEach { response -> add(response.data!!) }
            })
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