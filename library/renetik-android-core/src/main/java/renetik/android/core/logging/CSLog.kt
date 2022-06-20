package renetik.android.core.logging

import renetik.android.core.extensions.content.CSToast.toast
import renetik.android.core.kotlin.primitives.separateToString
import renetik.android.core.lang.CSEnvironment.isDebug
import java.lang.System.currentTimeMillis
import java.lang.Thread.currentThread
import java.text.DateFormat.getDateTimeInstance

object CSLog {
    private const val NoMessage = "No Message"

    private var instance: CSLogger = CSPrintLogger()

    fun init(logger: CSLogger) {
        instance = logger
    }

    fun logDebug(message: (() -> Any)? = null) {
        if (isDebug)
            instance.debug(*createDebugMessage(message?.invoke() ?: NoMessage))
    }

    fun logDebug(e: Throwable) = instance.debug(e)

    fun logWarn(vararg values: Any?) = instance.warn(*createMessage(values))
    fun logWarn(e: Throwable, vararg values: Any?) = instance.warn(e, *createMessage(values))
    fun logError(vararg values: Any?) = instance.error(*createMessage(values))
    fun logError(e: Throwable, vararg values: Any?) = instance.error(e, *createMessage(values))
    fun logInfo(vararg values: Any?) = instance.info(*createMessage(values))

    fun logInfoToast(vararg values: Any?) {
        instance.info(*createMessage(values))
        toast(" ".separateToString(*values))
    }

    fun logWarnToast(vararg values: Any?) {
        instance.warn(*createMessage(values))
        toast(" ".separateToString(*values))
    }

    fun logErrorToast(vararg values: Any?) {
        instance.error(*createMessage(values))
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