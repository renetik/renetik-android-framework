package renetik.android.network.operation

import renetik.android.core.lang.ArgFunc
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

    //TODO: Move to CSHttpRequestOperation....
    var isRefresh = false
    var isCached = true
    var expireMinutes: Int? = 1
    var isJustUseCache = false

    fun send(): CSProcess<Data> = createProcess(this).also { process ->
        this.process = process.parent(this).onSuccess { onSuccess() }
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

//TODO: Move to CSHttpRequestOperation....
fun <Data : Any> CSOperation<Data>.refresh() = apply { isRefresh = true }

fun <Data : Any> CSOperation<Data>.onSuccess(function: ArgFunc<CSProcess<Data>>) =
    apply { eventSuccess.listen(function) }

fun <Data : Any> CSOperation<Data>.onFailed(function: ArgFunc<CSProcess<*>>) =
    apply { eventFailed.listen(function) }

fun <Data : Any> CSOperation<Data>.onDone(function: ArgFunc<CSProcess<Data>>) =
    apply { eventDone.listen(function) }