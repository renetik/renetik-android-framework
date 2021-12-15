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


open class CSProcessBase<Data : Any>(var data: Data? = null) : CSContext() {

    private val eventSuccess = event<CSProcessBase<Data>>()
    fun onSuccess(function: (CSProcessBase<Data>) -> Unit) = apply { eventSuccess.listen(function) }

    private val eventCancel = event<CSProcessBase<Data>>()
    fun onCancel(function: (CSProcessBase<Data>) -> Unit) = apply { eventCancel.listen(function) }

    private val eventFailed = event<CSProcessBase<*>>()
    fun onFailed(function: (CSProcessBase<*>) -> Unit) = apply { eventFailed.listen(function) }

    private val eventDone = event<CSProcessBase<Data>>()
    fun onDone(function: (CSProcessBase<Data>) -> Unit) = apply { eventDone.listen(function) }

    private val onProgress = event<CSProcessBase<Data>>()

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
    var failedProcess: CSProcessBase<*>? = null
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

    fun failed(process: CSProcessBase<*>) {
        if (isCanceled) return
        onFailedImpl(process)
        onDoneImpl()
    }

    fun failed(message: String): CSProcessBase<Data> {
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

    private fun onFailedImpl(process: CSProcessBase<*>) {
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

    override fun toString() = "${super.toString()} data:$data"
}