package renetik.android.lang

import android.view.View
import renetik.android.model.application
import renetik.android.extensions.toast
import renetik.android.extensions.view.snackBarInfo
import renetik.java.collections.list
import renetik.java.extensions.primitives.separateToString

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

    fun logInfoSnack(view: View, vararg values: Any?) {
        val message = createLogMessage(values)
        view.snackBarInfo(" ".separateToString(*message))
        application.logger.info(*message)
    }

    private fun createLogMessage(values: Array<out Any?>): Array<Any?> {
        val element = Thread.currentThread().stackTrace[5]
        return list<Any?>("${element.className}(${element.fileName}:${element.lineNumber})")
                .putAll(*values).toTypedArray()
    }
}

interface CSLogger {

    fun onLowMemory()

    fun error(vararg values: Any?)

    fun error(e: Throwable, vararg values: Any?)

    fun info(vararg values: Any?)

    fun debug(vararg values: Any?)

    fun logString(): String

    fun warn(vararg values: Any?)

    fun warn(e: Throwable, vararg values: Any?)

}
