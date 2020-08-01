package renetik.android.logging

import android.util.Log
import android.util.Log.getStackTraceString
import android.widget.Toast
import renetik.android.base.CSApplicationInstance.application
import renetik.android.base.CSContextController
import renetik.android.java.common.CSDataConstants.MB
import renetik.android.java.event.event
import renetik.android.java.event.listen
import renetik.android.java.extensions.*
import renetik.android.logging.CSLogEventType.*
import java.lang.StringBuilder
import java.text.DateFormat
import java.util.*

class AndroidLogger() : CSContextController(), CSLogger {

    constructor(onLogEvent: (CSLogEvent) -> Unit) : this() {
        this.eventOnLog.listen(onLogEvent)
    }

    override val eventOnLog = event<CSLogEvent>()

    private val maxLogSize = 2.5 * MB
    private val dateFormat = DateFormat.getDateTimeInstance()
    private val logText = StringBuilder()

    override fun onLowMemory() {
        val sizeAboveLowMemoryMax = logText.length - MB
        if (sizeAboveLowMemoryMax > 0) logText.cut(0, sizeAboveLowMemoryMax)
    }

    override fun error(vararg values: Any?) {
        val message = createMessage(*values).toString()
        addMemoryMessage("${Error.title}: $message")
        Log.e(application.name, message)
        eventOnLog.fire(CSLogEvent(Error, message))
    }

    override fun error(e: Throwable, vararg values: Any?) {
        val message = createMessage(*values)
        addMemoryMessage("${Error.title}: $message ${getStackTraceString(e)}")
        val messageString = message.toString()
        Log.e(application.name, messageString)
        eventOnLog.fire(CSLogEvent(Error, messageString))
    }

    override fun info(vararg values: Any?) {
        val message = createMessage(*values).toString()
        addMemoryMessage(message)
        Log.i(application.name, message)
        eventOnLog.fire(CSLogEvent(Info, message))
    }

    private fun toast(message: String) {
        Toast.makeText(application, message, Toast.LENGTH_SHORT).show()
    }

    override fun debug(vararg values: Any?) {
        val message = createMessage(*values).toString()
        addMemoryMessage(message)
        Log.d(application.name, message)
        eventOnLog.fire(CSLogEvent(Debug, message))
    }

    override fun logString(): String {
        return logText.toString()
    }

    override fun warn(vararg values: Any?) {
        val message = createMessage(*values).toString()
        addMemoryMessage("${Warn.title}: $message")
        Log.w(application.name, message)
        eventOnLog.fire(CSLogEvent(Warn, message))
    }

    override fun warn(e: Throwable, vararg values: Any?) {
        val message = createMessage(*values)
        addMemoryMessage(message.addSpace().add(getStackTraceString(e)))
        Log.w(application.name, message.toString(), e)
        eventOnLog.fire(CSLogEvent(Warn, message.toString()))
    }

    private fun addMemoryMessage(message: CharSequence) {
        logText.add("- ").add(dateFormat.format(Date())).add(" - ").add(message).addLine()
        if (logText.length > maxLogSize) logText.cut(0, MB)
    }

    private fun createMessage(vararg values: Any?): StringBuilder {
        val message = StringBuilder()
        for (string in values) string.notNull { message.add(it).addSpace() }
        return message
    }

}
