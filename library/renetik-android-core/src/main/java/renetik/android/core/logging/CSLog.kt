package renetik.android.core.logging

import renetik.android.core.CSApplication.Companion.app
import renetik.android.core.extensions.content.CSToast.toast
import renetik.android.core.kotlin.primitives.separateToString
import java.lang.System.currentTimeMillis
import java.lang.Thread.currentThread
import java.text.DateFormat.getDateTimeInstance

object CSLog {
    private const val NoMessage = "No Message"

    private var log = AndroidLogger()

    fun logDebug(message: (() -> Any)? = null) {
        if (app.isDebugBuild) log.debug(*createDebugMessage(message?.invoke() ?: NoMessage))
    }

    fun logDebug(e: Throwable) = log.debug(e)

    fun logWarn(vararg values: Any?) = log.warn(*createMessage(values))
    fun logWarn(e: Throwable, vararg values: Any?) = log.warn(e, *createMessage(values))
    fun logError(vararg values: Any?) = log.error(*createMessage(values))
    fun logError(e: Throwable, vararg values: Any?) = log.error(e, *createMessage(values))
    fun logInfo(vararg values: Any?) = log.info(*createMessage(values))

    fun logInfoToast(vararg values: Any?) {
        log.info(*createMessage(values))
        toast(" ".separateToString(*values))
    }

    fun logWarnToast(vararg values: Any?) {
        log.warn(*createMessage(values))
        toast(" ".separateToString(*values))
    }

    fun logErrorToast(vararg values: Any?) {
        log.error(*createMessage(values))
        toast(" ".separateToString(*values))
    }

    private fun createMessage(values: Array<out Any?>): Array<out Any?> = Array(values.size + 2) {
        if (it == 0) time
        else if (it == 1) traceLine
        else values[it - 2]
    }

    private val timeFormat by lazy { getDateTimeInstance() }

    private val traceLine
        get() = currentThread().stackTrace[5].let { element ->
            "${element.className}$${element.methodName}(${element.fileName}:${element.lineNumber})"
        }

    private val time get() = timeFormat.format(currentTimeMillis())

    private fun createDebugMessage(values: Array<out Any?>) = Array(values.size + 2) {
        when (it) {
            0 -> time
            1 -> traceLine
            else -> values[it - 2]
        }
    }

    private fun createDebugMessage(message: Any?) = Array(3) {
        when (it) {
            0 -> time
            1 -> traceLine
            else -> message
        }
    }
}