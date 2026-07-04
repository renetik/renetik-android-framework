package renetik.android.core.logging

interface CSLoggerListener {
    fun onLogEvent(event: CSLogLevel, message: String)
}