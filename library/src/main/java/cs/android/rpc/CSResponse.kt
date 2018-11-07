package cs.android.rpc

import cs.android.extensions.execute
import cs.android.extensions.string
import cs.android.viewbase.CSContextController
import cs.java.lang.CSLang
import cs.java.lang.CSLang.*

open class CSResponse<Data : Any> : CSContextController {
    private val eventSuccess = CSLang.event<CSResponse<Data>>()
    private val eventFailed = CSLang.event<CSResponse<*>>()
    private val eventDone = CSLang.event<CSResponse<Data>>()
    val onProgress = CSLang.event<CSResponse<Data>>()
    var progress: Long = 0
        set(progress) {
            field = progress
            onProgress.fire(this)
        }
    var isSuccess: Boolean = false
    var isFailed: Boolean = false
    var isDone: Boolean = false
    var isCanceled: Boolean = false
    var url: String? = null
    var title: String? = null
    lateinit var data: Data
    var failedMessage: String? = null
    var failedResponse: CSResponse<*>? = null
    var exception: Throwable? = null

    constructor(url: String, data: Data) {
        this.url = url
        this.data = data
    }

    constructor(data: Data) {
        this.data = data
    }

    constructor() {
    }

    fun success() {
        if (isCanceled) return
        onSuccessImpl()
        onDoneImpl()
    }

    fun success(data: Data) {
        if (isCanceled) return
        this.data = data
        onSuccessImpl()
        onDoneImpl()
    }

    private fun onSuccessImpl() {
        info("Response onSuccessImpl", this, url)
        if (isFailed) error(exception("already failed"))
        if (isSuccess) error(exception("already success"))
        if (isDone) error(exception("already done"))
        isSuccess = YES
        fire(eventSuccess, this)
    }

    fun failed(response: CSResponse<*>) {
        if (isCanceled) return
        onFailedImpl(response)
        onDoneImpl()
    }

    fun failed(message: String): CSResponse<Data> {
        if (isCanceled) return this
        this.failedMessage = message
        failed(this)
        return this
    }

    fun failed(exception: Exception?, message: String) {
        if (isCanceled) return
        this.exception = exception
        this.failedMessage = message
        onFailedImpl(this)
        onDoneImpl()
    }

    private fun onFailedImpl(response: CSResponse<*>) {
        info("Response onFailedImpl", this, url)
        if (isDone) error(exception("already done"))
        if (isFailed) error(exception("already failed"))
        failedResponse = response
        isFailed = YES
        exception = if (set(response.exception)) response.exception else Throwable()
        failedMessage = string(" ", list(response.failedMessage, getRootCauseMessage(response.exception)))
        error(exception, failedMessage)
        eventFailed.fire(response)
    }

    open fun cancel() {
        info("Response cancel", this, "isCanceled", isCanceled, "isDone", isDone, "isSuccess", isSuccess, "isFailed", isFailed)
        if (isCanceled || isDone || isSuccess || isFailed) return
        isCanceled = YES
        onDoneImpl()
    }

    private fun onDoneImpl() {
        debug("Response onDone", this)
        if (isDone) {
            error(exception("already done"))
            return
        }
        isDone = YES
        fire(eventDone, this)
    }

    fun onSuccess(function: (argument: CSResponse<Data>) -> Unit): CSResponse<Data> {
        eventSuccess.execute(function)
        return this
    }

    fun onFailed(function: (argument: CSResponse<*>) -> Unit): CSResponse<Data> {
        eventFailed.execute(function)
        return this
    }

    fun onDone(function: (argument: CSResponse<Data>) -> Unit): CSResponse<Data> {
        eventDone.execute(function)
        return this
    }
}

