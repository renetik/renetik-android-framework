package renetik.android.rpc

import renetik.android.extensions.YES
import renetik.android.extensions.string
import renetik.android.java.collections.list
import renetik.android.java.event.event
import renetik.android.java.event.execute
import renetik.android.lang.CSLang.*
import renetik.android.lang.info
import renetik.android.viewbase.CSContextController

open class CSResponse<Data : Any> : CSContextController {
    private val eventSuccess = event<CSResponse<Data>>()
    fun onSuccess(function: (CSResponse<Data>) -> Unit) = apply { eventSuccess.execute(function) }

    private val eventFailed = event<CSResponse<*>>()
    fun onFailed(function: (CSResponse<*>) -> Unit) = apply { eventFailed.execute(function) }

    private val eventDone = event<CSResponse<Data>>()
    fun onDone(function: (CSResponse<Data>) -> Unit) = apply { eventDone.execute(function) }

    val onProgress = event<CSResponse<Data>>()
    var progress: Long = 0
        set(progress) {
            field = progress
            onProgress.fire(this)
        }
    var isSuccess = false
    var isFailed = false
    var isDone = false
    var isCanceled = false
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
        eventSuccess.fire(this)
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

    fun failed(exception: Throwable?, message: String?) {
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
        eventDone.fire(this)
    }
}

