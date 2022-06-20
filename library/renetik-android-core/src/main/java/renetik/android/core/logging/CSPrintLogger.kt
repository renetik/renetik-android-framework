package renetik.android.core.logging

import renetik.android.core.BuildConfig.DEBUG
import renetik.android.core.BuildConfig.LIBRARY_PACKAGE_NAME
import renetik.android.core.kotlin.text.add
import renetik.android.core.kotlin.text.addSpace
import renetik.android.core.lang.void
import renetik.android.core.logging.CSLoggerEvent.*

class CSPrintLogger(val name: String = LIBRARY_PACKAGE_NAME,
                    private val isDebug: Boolean = DEBUG,
                    val listener: ((event: CSLoggerEvent,
                                    message: String) -> void)? = null) : CSLogger {


    override fun error(vararg values: Any?) {
        val message = createMessage(*values).toString()
        println("$Error: $message")
        listener?.invoke(Error, message)
    }

    override fun error(e: Throwable, vararg values: Any?) {
        val message = createMessage(*values)
        println("$Error: $name: $message $e")
        listener?.invoke(Error, message.addSpace().add(e.asTraceString).toString())
    }

    override fun info(vararg values: Any?) {
        val message = createMessage(*values).toString()
        println("$Info: $name: $message")
        listener?.invoke(Info, message)
    }

    override fun debug(vararg values: Any?) {
        if (!isDebug) return
        val message = createMessage(*values).toString()
        println("$Debug: $name: $message")
        listener?.invoke(Debug, message)
    }

    override fun debug(e: Throwable, vararg values: Any?) {
        if (!isDebug) return
        val message = createMessage(*values)
        println("$Debug: $name: $message $e")
        listener?.invoke(Debug, message.addSpace().add(e.asTraceString).toString())
    }

    override fun warn(vararg values: Any?) {
        val message = createMessage(*values).toString()
        println("$Warn: $name: $message")
        listener?.invoke(Warn, message)
    }

    override fun warn(e: Throwable, vararg values: Any?) {
        val message = createMessage(*values)
        println("$Warn: $name: $message $e")
        listener?.invoke(Warn, message.addSpace().add(e.asTraceString).toString())
    }

    private fun createMessage(vararg values: Any?) = StringBuilder().apply {
        values.forEach { it?.let { add(it).addSpace() } }
    }
}
