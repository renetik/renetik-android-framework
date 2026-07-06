package renetik.android.core.logging

import renetik.android.core.BuildConfig.LIBRARY_PACKAGE_NAME
import renetik.android.core.kotlin.asTraceString
import renetik.android.core.logging.CSLogLevel.Debug
import renetik.android.core.logging.CSLogLevel.Error
import renetik.android.core.logging.CSLogLevel.Info
import renetik.android.core.logging.CSLogLevel.Warn

class CSPrintLogger(
    val name: String = LIBRARY_PACKAGE_NAME,
    override val level: CSLogLevel = Debug,
    val listener: CSLogListener? = null
) : CSLogger {

    override fun verbose(e: Throwable?, message: String?) {
        println("$Debug: $name: $message $e ${e?.asTraceString ?: ""}")
        println(createPrintMessage(Debug, message, e))
        listener?.invoke(Debug, message.toString(), e)
    }

    override fun debug(e: Throwable?, message: String?) {
        println(createPrintMessage(Debug, message, e))
        listener?.invoke(Debug, message.toString(), e)
    }

    override fun info(e: Throwable?, message: String?) {
        println(createPrintMessage(Info, message, e))
        listener?.invoke(Info, message.toString(), e)
    }

    override fun warn(e: Throwable?, message: String?) {
        println(createPrintMessage(Warn, message, e))
        listener?.invoke(Warn, message.toString(), e)
    }

    override fun error(e: Throwable?, message: String?) {
        println(createPrintMessage(Error, message, e))
        listener?.invoke(Error, message.toString(), e)
    }

    private fun createPrintMessage(
        level: CSLogLevel, message: String?, e: Throwable?
    ): String = "$level: $name: $message $e ${e?.asTraceString ?: ""}"
}