package renetik.android.client.request

import renetik.android.framework.CSContext
import renetik.android.framework.event.event
import renetik.android.framework.event.listen
import renetik.kotlin.notNull

open class CSOperation<Data : Any>() : CSContext() {

    var executeProcess: (CSOperation<Data>.() -> CSProcessBase<Data>)? = null

    constructor(function: CSOperation<Data>.() -> CSProcessBase<Data>) : this() {
        executeProcess = function
    }

    open fun executeProcess(): CSProcessBase<Data> {
        return executeProcess!!.invoke(this)
    }

    private val eventSuccess = event<Data>()
    private val eventFailed = event<CSProcessBase<*>>()
    private val eventDone = event<Data?>()
    var process: CSProcessBase<Data>? = null
    var isRefresh = false
    var isCached = true
    var expireMinutes: Int? = 1
    var isJustUseCache = false

    fun refresh() = apply { isRefresh = true }

    fun onSuccess(function: (argument: Data) -> Unit) =
        apply { eventSuccess.listen(function) }

    fun onFailed(function: (argument: CSProcessBase<*>) -> Unit) =
        apply { eventFailed.listen(function) }

    fun onDone(function: (argument: Data?) -> Unit) =
        apply { eventDone.listen(function) }

    fun send(): CSProcessBase<Data> = executeProcess().also { process ->
        this.process = process
        process.onSuccess {
            eventSuccess.fire(process.data!!)
            eventDone.fire(process.data)
        }
    }

    fun cancel() {
        process.notNull {
            if (it.isFailed) {
                eventFailed.fire(it)
                eventDone.fire(null)
            } else {
                it.cancel()
                eventDone.fire(null)
            }
        }
    }


}

