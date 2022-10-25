package renetik.android.network.operation

import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.CSModel
import renetik.android.event.common.parent
import renetik.android.network.process.CSProcess

open class CSOperation<Data : Any>(
    private val createProcess: CSOperation<Data>.() -> CSProcess<Data>)
    : CSModel() {

    val eventSuccess = event<CSProcess<Data>>()
    val eventFailed = event<CSProcess<*>>()
    val eventDone = event<CSProcess<Data>>()
    lateinit var process: CSProcess<Data>

    fun send(): CSProcess<Data> = createProcess(this).also {
        process = it.parent(this).onSuccess { onSuccess() }
    }

    fun cancel() {
        if (process.isFailed) eventFailed.fire(process.failedProcess!!)
        else process.cancel()
        onDone()
    }

    private fun onSuccess() {
        eventSuccess.fire(process)
        onDone()
    }

    private fun onDone() {
        eventDone.fire(process)
        onDestruct()
    }
}