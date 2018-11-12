package renetik.android.lang

import android.util.Log
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import renetik.android.application
import renetik.android.java.lang.CSTextInterface
import renetik.android.lang.CSLang.*
import renetik.android.viewbase.CSContextController
import java.text.DateFormat
import java.util.*

class CrashlyticsLogger() : CSContextController(), CSLogger {

    private val maxLogSize = 2.5 * MB
    private val dateFormat = DateFormat.getDateTimeInstance()
    private val logText = textBuilder()

    override fun onLowMemory() {
        val sizeAboveLowMemoryMax = logText.length - MB
        if (sizeAboveLowMemoryMax > 0) logText.cut(0, sizeAboveLowMemoryMax)
    }

    override fun error(vararg values: Any) {
        val message = createMessage(*values).toString()
        addMemoryMessage("Error: $message")
//        Log.e(model.applicationName(), message)
        Crashlytics.log(Log.ERROR, application.name, message)
        if (isDebugMode()) toast(message)
    }

    override fun error(e: Throwable, vararg values: Any) {
        val message = createMessage(*values)
        addMemoryMessage("Error: " + message.addSpace().add(createTraceString(e)))
        val messageString = message.toString()
//        Log.e(model.applicationName(), messageString)
        Crashlytics.log(Log.ERROR, application.name, messageString)
        Crashlytics.logException(e)
        if (isDebugMode()) toast(messageString)
    }

    override fun info(vararg values: Any) {
        val message = createMessage(*values).toString()
        addMemoryMessage(message)
//        Log.i(model.applicationName(), message)
        Crashlytics.log(Log.INFO, application.name, message)
        if (isDebugMode()) toast(message)
    }

    private fun toast(message: String) {
        Toast.makeText(application, message, Toast.LENGTH_SHORT).show()
    }

    override fun debug(vararg values: Any) {
        val message = createMessage(*values).toString()
        if (isDebugMode()) addMemoryMessage(message)
//        Log.d(model.applicationName(), message)
        Crashlytics.log(Log.DEBUG, application.name, message)
    }

    override fun logString(): String {
        return logText.toString()
    }

    override fun warn(vararg values: Any?) {
        val message = createMessage(*values).toString()
        addMemoryMessage("Warn: $message")
//        Log.w(model.applicationName(), message)
        Crashlytics.log(Log.WARN, application.name, message)
        if (isDebugMode()) toast(message)
    }

    override fun warn(e: Throwable, vararg values: Any?) {
        val message = createMessage(*values)
        addMemoryMessage(message.addSpace().add(createTraceString(e)))
//        Log.w(model.applicationName(), message.toString(), e)
        Crashlytics.log(Log.WARN, application.name, message.toString())
        if (isDebugMode()) toast(message.toString())
    }

    private fun addMemoryMessage(message: CharSequence) {
        logText.add("- ").add(dateFormat.format(Date())).add(" - ").add(message).addLine()
        if (logText.length > maxLogSize) logText.cut(0, MB)
    }

    private fun createMessage(vararg values: Any?): CSTextInterface {
        val message = textBuilder()
        for (string in values) message.add(string).addSpace()
        return message
    }

}
