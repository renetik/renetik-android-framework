package renetik.android.logging

import renetik.android.framework.event.CSEvent

enum class CSLogEventType(val title: String) {
    Warn("Warn"), Info("Info"), Error("Error"), Debug("Debug")
}

data class CSLogEvent(val type: CSLogEventType, val message: String)

interface CSLogger {

    val eventOnLog: CSEvent<CSLogEvent>

    fun onLowMemory()

    fun error(vararg values: Any?)

    fun error(e: Throwable, vararg values: Any?)

    fun info(vararg values: Any?)

    fun debug(vararg values: Any?)

    fun logString(): String

    fun warn(vararg values: Any?)

    fun warn(e: Throwable, vararg values: Any?)

}