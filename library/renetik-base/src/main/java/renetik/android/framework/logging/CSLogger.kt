package renetik.android.framework.logging

interface CSLogger {
    fun error(vararg values: Any?)

    fun error(e: Throwable, vararg values: Any?)

    fun info(vararg values: Any?)

    fun debug(vararg values: Any?)

    fun debug(e: Throwable, vararg values: Any?)

    fun warn(vararg values: Any?)

    fun warn(e: Throwable, vararg values: Any?)
}