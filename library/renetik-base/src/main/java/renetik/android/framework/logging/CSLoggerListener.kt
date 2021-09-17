package renetik.android.framework.logging

interface CSLoggerListener {
    fun onLogEvent(event: CSLoggerEvent, message: String)
}