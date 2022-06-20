package renetik.android.network.operation

import renetik.android.network.process.CSProcessBase
import renetik.android.core.kotlin.notNull
import renetik.android.core.lang.ArgFunc
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.listen
import renetik.android.event.owner.CSContext
import renetik.android.event.registration.CSRegistration

open class CSOperation<Data : Any>() : CSContext(), CSRegistration {

    var executeProcess: (CSOperation<Data>.() -> CSProcessBase<Data>)? = null

    constructor(function: CSOperation<Data>.() -> CSProcessBase<Data>) : this() {
        executeProcess = function
    }

    open fun executeProcess(): CSProcessBase<Data> {
        return executeProcess!!.invoke(this)
    }

    private val eventSuccess = event<CSProcessBase<Data>>()
    private val eventFailed = event<CSProcessBase<*>>()
    private val eventDone = event<CSProcessBase<Data>>()
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

    override var isActive = false

    fun send(): CSProcessBase<Data> = executeProcess().also { process ->
        this.process = process
        isActive = true
        process.onSuccess {
            eventSuccess.fire(process)
            eventDone.fire(process)
            isActive = false
        }
    }

    override fun cancel() {
        process.notNull {
            if (it.isFailed) {
                eventFailed.fire(it.failedProcess!!)
                eventDone.fire(it)
            } else {
                it.cancel()
                eventDone.fire(it)
            }
        }
        isActive = false
    }
}

