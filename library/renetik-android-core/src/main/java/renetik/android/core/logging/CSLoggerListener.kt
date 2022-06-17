package renetik.android.core.logging

interface CSLoggerListener {
    fun onLogEvent(event: CSLoggerEvent, message: String)
}