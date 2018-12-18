package renetik.android.lang

import android.util.Log.*
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import renetik.android.model.application
import renetik.java.extensions.notNull
import renetik.android.view.base.CSContextController
import renetik.java.extensions.*
import renetik.java.lang.CSLang
import java.text.DateFormat
import java.util.*

class CrashlyticsLogger() : CSContextController(), CSLogger {

    private val maxLogSize = 2.5 * CSLang.MB
    private val dateFormat = DateFormat.getDateTimeInstance()
    private val logText = StringBuilder()

    override fun onLowMemory() {
        val sizeAboveLowMemoryMax = logText.length - CSLang.MB
        if (sizeAboveLowMemoryMax > 0) logText.cut(0, sizeAboveLowMemoryMax)
    }

    override fun error(vararg values: Any?) {
        val message = createMessage(*values).toString()
        addMemoryMessage("Error: $message")
        Crashlytics.log(ERROR, application.name, message)
    }

    override fun error(e: Throwable, vararg values: Any?) {
        val message = createMessage(*values)
        addMemoryMessage("Error: " + message.addSpace().add(getStackTraceString(e)))
        val messageString = message.toString()
        Crashlytics.log(ERROR, application.name, messageString)
        Crashlytics.logException(e)
    }

    override fun info(vararg values: Any?) {
        val message = createMessage(*values).toString()
        addMemoryMessage(message)
        Crashlytics.log(INFO, application.name, message)
    }

    private fun toast(message: String) {
        Toast.makeText(application, message, Toast.LENGTH_SHORT).show()
    }

    override fun debug(vararg values: Any?) {
        val message = createMessage(*values).toString()
        addMemoryMessage(message)
        Crashlytics.log(DEBUG, application.name, message)
    }

    override fun logString(): String {
        return logText.toString()
    }

    override fun warn(vararg values: Any?) {
        val message = createMessage(*values).toString()
        addMemoryMessage("Warn: $message")
        Crashlytics.log(WARN, application.name, message)
    }

    override fun warn(e: Throwable, vararg values: Any?) {
        val message = createMessage(*values)
        addMemoryMessage(message.addSpace().add(getStackTraceString(e)))
        Crashlytics.log(WARN, application.name, message.toString())
    }

    private fun addMemoryMessage(message: CharSequence) {
        logText.add("- ").add(dateFormat.format(Date())).add(" - ").add(message).addLine()
        if (logText.length > maxLogSize) logText.cut(0, CSLang.MB)
    }

    private fun createMessage(vararg values: Any?): StringBuilder {
        val message = StringBuilder()
        for (string in values) string.notNull { message.add(it).addSpace() }
        return message
    }

}
