package renetik.android.framework.logging

import android.util.Log
import android.util.Log.getStackTraceString
import android.widget.Toast
import renetik.android.framework.CSApplication.Companion.application
import renetik.android.framework.CSContext
import renetik.android.framework.lang.CSDataConstants.MB
import renetik.android.framework.event.event
import renetik.android.framework.event.listen
import renetik.android.framework.logging.CSLogEventType.*
import renetik.kotlin.notNull
import renetik.kotlin.text.add
import renetik.kotlin.text.addLine
import renetik.kotlin.text.addSpace
import renetik.kotlin.text.cut
import java.lang.StringBuilder
import java.text.DateFormat
import java.util.*

class AndroidLogger() : CSContext(), CSLogger {

    override val eventOnLog = event<CSLogEvent>()

    private val maxLogSize = 2.5 * MB
    private val dateFormat = DateFormat.getDateTimeInstance()
    private val logText = StringBuilder()

    constructor(onLogListener: (CSLogEvent) -> Unit) : this() {
        eventOnLog.listen(onLogListener)
    }

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
        if (!application.isDebugBuild) return
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
