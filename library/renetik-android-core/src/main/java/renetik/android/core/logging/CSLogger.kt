package renetik.android.core.logging

import java.io.PrintWriter
import java.io.StringWriter

interface CSLogger {
    fun error(vararg values: Any?)

    fun error(e: Throwable, vararg values: Any?)

    fun info(vararg values: Any?)

    fun debug(vararg values: Any?)

    fun debug(e: Throwable, vararg values: Any?)

    fun warn(vararg values: Any?)

    fun warn(e: Throwable, vararg values: Any?)

    val Throwable?.asTraceString: String
        get() {
            if (this == null) return ""
            val stringWriter = StringWriter()
            val printWriter = PrintWriter(stringWriter)
            printStackTrace(printWriter)
            printWriter.flush()
            return stringWriter.toString()
        }

}