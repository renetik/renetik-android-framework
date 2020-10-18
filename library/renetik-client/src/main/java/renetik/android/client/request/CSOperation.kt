package renetik.android.client.request

import renetik.android.base.CSContextController
import renetik.android.java.event.event
import renetik.android.java.event.listen
import renetik.android.java.extensions.notNull

open class CSOperation<Data : Any>() : CSContextController() {

    var executeProcess: (CSOperation<Data>.() -> CSProcess<Data>)? = null

    constructor(function: CSOperation<Data>.() -> CSProcess<Data>) : this() {
        executeProcess = function
    }

    open fun executeProcess(): CSProcess<Data> {
        return executeProcess!!.invoke(this)
    }

    private val eventSuccess = event<Data>()
    private val eventFailed = event<CSProcess<*>>()
    private val eventDone = event<Data?>()
    var process: CSProcess<Data>? = null
    var isRefresh = false
    var isCached = true
    var expireMinutes: Int? = 1
    var isJustUseCache = false

    fun refresh() = apply { isRefresh = true }

    fun onSuccess(function: (argument: Data) -> Unit) =
        apply { eventSuccess.listen(function) }

    fun onFailed(function: (argument: CSProcess<*>) -> Unit) =
        apply { eventFailed.listen(function) }

    fun onDone(function: (argument: Data?) -> Unit) =
        apply { eventDone.listen(function) }

    fun send(): CSProcess<Data> = executeProcess().also { process ->
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

