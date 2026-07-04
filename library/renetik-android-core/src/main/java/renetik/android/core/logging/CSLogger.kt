package renetik.android.core.logging

interface CSLogger {
    val level: CSLogLevel
    fun verbose(e: Throwable?, message: String?)
    fun debug(e: Throwable?, message: String?)
    fun info(e: Throwable?, message: String?)
    fun warn(e: Throwable?, message: String?)
    fun error(e: Throwable?, message: String?)
}