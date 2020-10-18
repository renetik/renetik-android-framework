package renetik.android.logging

import renetik.android.base.CSApplicationObject.application
import renetik.android.extensions.toast
import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.collections.putAll
import renetik.android.java.extensions.primitives.separateToString

object CSLog {

    //TODO: I could not make this work somehow...
    // When view is created by android studio layout editor there is no application
//    val defaultLogger = AndroidLogger()

    val logger: CSLogger
        get() = application.logger

    fun logDebug(vararg values: Any?) =
        logger.debug(*createLogMessage(values))

    fun logWarn(vararg values: Any?) =
        logger.warn(*createLogMessage(values))

    fun logWarn(e: Throwable, vararg values: Any?) =
        logger.warn(e, *createLogMessage(values))

    fun logError(vararg values: Any?) =
        logger.error(*createLogMessage(values))

    fun logError(e: Throwable, vararg values: Any?) =
        logger.error(e, *createLogMessage(values))

    fun logInfo(vararg values: Any?) =
        logger.info(*createLogMessage(values))

    fun logInfoToast(vararg values: Any?) {
        val message = createLogMessage(values)
        toast(" ".separateToString(*message))
        logger.info(*message)
    }

    private fun createLogMessage(values: Array<out Any?>): Array<Any?> {
        val element = Thread.currentThread().stackTrace[4]
        return list<Any?>("${element.className}(${element.fileName}:${element.lineNumber})")
            .putAll(*values).toTypedArray()
    }
}

