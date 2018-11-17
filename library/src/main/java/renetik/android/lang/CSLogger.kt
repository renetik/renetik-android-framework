package renetik.android.lang

import renetik.android.application
import renetik.android.java.collections.list

private val level = 3

fun warn(vararg values: Any?) {
    val element = Thread.currentThread().stackTrace[level]
    application.logger.warn(*createLogMessage(element, values))
}

fun warn(e: Throwable, vararg values: Any?) {
    val element = Thread.currentThread().stackTrace[level]
    application.logger.warn(e, *createLogMessage(element, values))
}

fun error(vararg values: Any?) {
    val element = Thread.currentThread().stackTrace[level]
    application.logger.error(*createLogMessage(element, values))
}

fun error(e: Throwable, vararg values: Any?) {
    val element = Thread.currentThread().stackTrace[level]
    application.logger.error(e, *createLogMessage(element, values))
}

fun info(vararg values: Any?) {
    val element = Thread.currentThread().stackTrace[level]
    application.logger.info(*createLogMessage(element, values))
}

private fun createLogMessage(element: StackTraceElement, values: Array<out Any?>) =
        list<Any?>("${element.className}(${element.fileName}:${element.lineNumber})")
                .append(*values).toTypedArray()

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
