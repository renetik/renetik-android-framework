package renetik.android.framework.logging

import renetik.android.content.CSToast.toast
import renetik.android.framework.CSApplication.Companion.application
import renetik.android.primitives.separateToString
import java.lang.System.currentTimeMillis
import java.lang.Thread.currentThread
import java.text.DateFormat
import java.text.DateFormat.getDateTimeInstance

object CSLog {

    private val log by lazy { application.log }

    fun debug(vararg values: Any?) {
        if (application.isDebugBuild) log.debug(*createDebugMessage(values))
    }

    fun warn(vararg values: Any?) = log.warn(*createMessage(values))
    fun warn(e: Throwable, vararg values: Any?) = log.warn(e, *createMessage(values))
    fun error(vararg values: Any?) = log.error(*createMessage(values))
    fun error(e: Throwable, vararg values: Any?) = log.error(e, *createMessage(values))
    fun info(vararg values: Any?) = log.info(*createMessage(values))

    fun infoToast(vararg values: Any?) {
        val message = createMessage(values)
        toast(" ".separateToString(*values))
        log.info(*message)
    }

    private fun createMessage(values: Array<out Any?>): Array<out Any?> = Array(values.size + 2) {
        when (it) {
            0 -> time
            1 -> traceLine
            else -> values[it - 2]
        }
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
}