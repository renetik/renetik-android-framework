package cs.android.lang


import android.util.Log

import java.text.DateFormat
import java.util.Date

import cs.android.viewbase.CSContextController
import cs.java.lang.CSTextInterface

import android.util.Log.e
import android.util.Log.i
import android.util.Log.w
import com.crashlytics.android.Crashlytics.log
import com.crashlytics.android.Crashlytics.logException
import cs.java.lang.CSLang.*

class FabricLogger(private val model: CSModel) : CSContextController(), CSLogger {

    private val maxLogSize = 2.5 * MB
    private val dateFormat = DateFormat.getDateTimeInstance()
    private val logText = string()

    override fun onLowMemory() {
        val sizeAboveLowMemoryMax = logText.length - MB
        if (sizeAboveLowMemoryMax > 0) logText.cut(0, sizeAboveLowMemoryMax)
    }

    override fun error(vararg values: Any) {
        val message = createMessage(*values).toString()
        addMemoryMessage("Error: $message")
        e(model.name(), message)
        log(Log.ERROR, model.name(), message)
    }

    override fun error(e: Throwable, vararg values: Any) {
        val message = createMessage(*values)
        addMemoryMessage("Error: " + message.addSpace().add(createTraceString(e)))
        val messageString = message.toString()
        e(model.name(), messageString)
        log(Log.ERROR, model.name(), messageString)
        logException(e)
    }

    override fun info(vararg values: Any) {
        val message = createMessage(*values).toString()
        addMemoryMessage(message)
        i(model.name(), message)
        log(message)
    }

    override fun logString(): String {
        return logText.toString()
    }

    override fun warn(vararg values: Any) {
        val message = createMessage(*values).toString()
        addMemoryMessage("Warn: $message")
        w(model.name(), message)
        log(Log.WARN, model.name(), message)
    }

    override fun warn(e: Throwable, vararg values: Any) {
        val message = createMessage(*values)
        addMemoryMessage(message.addSpace().add(createTraceString(e)))
        w(model.name(), message.toString(), e)
        log(Log.WARN, model.name(), message.toString())
    }

    private fun addMemoryMessage(message: CharSequence) {
        logText.add("- ").add(dateFormat.format(Date())).add(" - ").add(message).addLine()
        if (logText.length > maxLogSize) logText.cut(0, MB)
    }

    private fun createMessage(vararg values: Any): CSTextInterface {
        val message = string()
        for (string in values) message.add(string).addSpace()
        return message
    }
}
