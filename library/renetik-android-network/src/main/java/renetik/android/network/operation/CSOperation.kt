package renetik.android.network.operation

import renetik.android.core.kotlin.notNull
import renetik.android.core.lang.ArgFunc
import renetik.android.core.lang.CSLeakCanary.expectWeaklyReachable
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.CSContext
import renetik.android.network.process.CSProcessBase

open class CSOperation<Data : Any>() : CSContext() {

    var executeProcess: (CSOperation<Data>.() -> CSProcessBase<Data>)? = null

    constructor(function: CSOperation<Data>.() -> CSProcessBase<Data>) : this() {
        executeProcess = function
    }

    open fun executeProcess(): CSProcessBase<Data> {
        return executeProcess!!.invoke(this)
    }

    val eventSuccess = event<CSProcessBase<Data>>()
    val eventFailed = event<CSProcessBase<*>>()
    val eventDone = event<CSProcessBase<Data>>()
    var process: CSProcessBase<Data>? = null
    var isRefresh = false
    var isCached = true
    var expireMinutes: Int? = 1
    var isJustUseCache = false

    fun refresh() = apply { isRefresh = true }

    fun onSuccess(function: ArgFunc<CSProcessBase<Data>>) =
        apply { eventSuccess.listen(function) }

    fun onFailed(function: ArgFunc<CSProcessBase<*>>) =
        apply { eventFailed.listen(function) }

    fun onDone(function: ArgFunc<CSProcessBase<Data>>) =
        apply { eventDone.listen(function) }

    var isActive = false

    fun send(): CSProcessBase<Data> = executeProcess().also { process ->
        this.process = process
        isActive = true
        process.onSuccess {
            eventSuccess.fire(process)
            onDone(process)
            isActive = false
        }
    }

    fun cancel() {
        process.notNull {
            if (it.isFailed) {
                eventFailed.fire(it.failedProcess!!)
                onDone(it)
            } else {
                it.cancel()
                onDone(it)
            }
        }
        isActive = false
    }

    private fun onDone(process: CSProcessBase<Data>) {
        eventDone.fire(process)
        expectWeaklyReachable("CSOperation $this onDone")
    }
}

