package renetik.android.framework.logging

import renetik.android.content.CSToast.toast
import renetik.android.framework.CSApplication.Companion.application
import renetik.android.primitives.separateToString
import java.lang.Thread.currentThread

object CSLog {

    private val log by lazy { application.log }

    fun debug(vararg values: Any?) {
        if (application.isDebugBuild) log.debug(*createLogMessage(values))
    }

    fun warn(vararg values: Any?) =
        log.warn(*createLogMessage(values))

    fun warn(e: Throwable, vararg values: Any?) =
        log.warn(e, *createLogMessage(values))

    fun error(vararg values: Any?) =
        log.error(*createLogMessage(values))

    fun error(e: Throwable, vararg values: Any?) =
        log.error(e, *createLogMessage(values))

    fun info(vararg values: Any?) =
        log.info(*createLogMessage(values))

    fun infoToast(vararg values: Any?) {
        val message = createLogMessage(values)
        toast(" ".separateToString(*message))
        log.info(*message)
    }

    private fun createLogMessage(values: Array<out Any?>): Array<Any?> {
        val element = currentThread().stackTrace[4]
        val trace = "${element.className}(${element.fileName}:${element.lineNumber})"
        return Array(values.size + 1) { index -> if (index == 0) trace else values[index - 1] }
    }
}

