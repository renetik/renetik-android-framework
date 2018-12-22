package renetik.android.logging

import renetik.android.base.application
import renetik.android.extensions.toast
import renetik.android.java.collections.list
import renetik.android.java.extensions.primitives.separateToString

object CSLog {

    fun logDebug(vararg values: Any?) =
            application.logger.debug(*createLogMessage(values))

    fun logWarn(vararg values: Any?) =
            application.logger.warn(*createLogMessage(values))

    fun logWarn(e: Throwable, vararg values: Any?) =
            application.logger.warn(e, *createLogMessage(values))

    fun logError(vararg values: Any?) =
            application.logger.error(*createLogMessage(values))

    fun logError(e: Throwable, vararg values: Any?) =
            application.logger.error(e, *createLogMessage(values))

    fun logInfo(vararg values: Any?) =
            application.logger.info(*createLogMessage(values))

    fun logInfoToast(vararg values: Any?) {
        val message = createLogMessage(values)
        toast(" ".separateToString(*message))
        application.logger.info(*message)
    }

    private fun createLogMessage(values: Array<out Any?>): Array<Any?> {
        val element = Thread.currentThread().stackTrace[4]
        return list<Any?>("${element.className}(${element.fileName}:${element.lineNumber})")
                .putAll(*values).toTypedArray()
    }
}

