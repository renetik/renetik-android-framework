package renetik.android.logging

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