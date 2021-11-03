package renetik.android.client.request

import renetik.android.framework.CSContext
import renetik.android.framework.event.event
import renetik.android.framework.event.listen
import renetik.android.framework.logging.CSLog.debug
import renetik.android.framework.logging.CSLog.error
import renetik.android.framework.logging.CSLog.info
import renetik.android.framework.logging.CSLog.warn
import renetik.android.framework.util.CSSynchronizedProperty.Companion.synchronized
import renetik.kotlin.exception
import renetik.kotlin.rootCauseMessage

open class CSProcess<Data : Any>(var data: Data? = null) : CSContext() {

    private val eventSuccess = event<CSProcess<Data>>()
    fun onSuccess(function: (CSProcess<Data>) -> Unit) = apply { eventSuccess.listen(function) }

    private val eventCancel = event<CSProcess<Data>>()
    fun onCancel(function: (CSProcess<Data>) -> Unit) = apply { eventCancel.listen(function) }

    private val eventFailed = event<CSProcess<*>>()
    fun onFailed(function: (CSProcess<*>) -> Unit) = apply { eventFailed.listen(function) }

    private val eventDone = event<CSProcess<Data>>()
    fun onDone(function: (CSProcess<Data>) -> Unit) = apply { eventDone.listen(function) }

    private val onProgress = event<CSProcess<Data>>()

    var progress: Long = 0
        set(progress) {
            field = progress
            onProgress.fire(this)
        }
    var isSuccess = false
    var isFailed = false
    var isDone = false
    var isCanceled by synchronized(false)
    var title: String? = null
    var failedMessage: String? = null
    var failedProcess: CSProcess<*>? = null
    var throwable: Throwable? = null

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
        info("Response onSuccessImpl", this)
        if (isFailed) error(exception("already failed"))
        if (isSuccess) error(exception("already success"))
        if (isDone) error(exception("already done"))
        isSuccess = true
        eventSuccess.fire(this)
    }

    fun failed(process: CSProcess<*>) {
        if (isCanceled) return
        onFailedImpl(process)
        onDoneImpl()
    }

    fun failed(message: String): CSProcess<Data> {
        if (isCanceled) return this
        this.failedMessage = message
        failed(this)
        return this
    }

    fun failed(exception: Throwable?, message: String? = null) {
        if (isCanceled) return
        this.throwable = exception
        this.failedMessage = message
        failed(this)
    }

    private fun onFailedImpl(process: CSProcess<*>) {
        if (isDone) error(exception("already done"))
        if (isFailed) error(exception("already failed"))
        else isFailed = true
        failedProcess = process
        failedMessage = process.failedMessage
        process.throwable?.rootCauseMessage?.let { warn(it) }
        throwable = process.throwable ?: Throwable()
        error(throwable!!, failedMessage)
        eventFailed.fire(process)
    }

    open fun cancel() {
        debug(
            "Response cancel", this, "isCanceled", isCanceled,
            "isDone", isDone, "isSuccess", isSuccess, "isFailed", isFailed
        )
        if (isCanceled || isDone || isSuccess || isFailed) return
        isCanceled = true
        eventCancel.fire(this)
        onDoneImpl()
    }

    private fun onDoneImpl() {
        debug("Response onDone", this)
        if (isDone) {
            error(exception("already done"))
            return
        }
        isDone = true
        eventDone.fire(this)
    }
}

open class CSHttpProcess<Data : Any>(url: String, data: Data) : CSProcess<Data>(data) {

    var url: String? = url

    override fun toString(): String {
        return super.toString() + url
    }
}