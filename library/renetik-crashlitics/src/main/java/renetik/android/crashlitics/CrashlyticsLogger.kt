package renetik.android.crashlitics

import android.util.Log.*
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import renetik.android.base.CSContextController
import renetik.android.base.application
import renetik.android.java.common.CSConstants.MB
import renetik.android.java.extensions.*
import renetik.android.logging.CSLogger
import java.text.DateFormat
import java.util.*

class CrashlyticsLogger : CSContextController(), CSLogger {

    private val maxLogSize = 2.5 * MB
    private val dateFormat = DateFormat.getDateTimeInstance()
    private val logText = StringBuilder()

    override fun onLowMemory() {
        val sizeAboveLowMemoryMax = logText.length - MB
        if (sizeAboveLowMemoryMax > 0) logText.cut(0, sizeAboveLowMemoryMax)
    }

    override fun error(vararg values: Any?) {
        val message = createMessage(*values).toString()
        addMemoryMessage("Error: $message")
        if (Fabric.isInitialized()) Crashlytics.log(ERROR, application.name, message)
        else e(application.name, message)
    }

    override fun error(e: Throwable, vararg values: Any?) {
        val message = createMessage(*values)
        addMemoryMessage("Error: " + message.addSpace().add(getStackTraceString(e)))
        val messageString = message.toString()
        if (Fabric.isInitialized()) {
            Crashlytics.log(ERROR, application.name, messageString)
            Crashlytics.logException(e)
        } else e(application.name, messageString)
    }

    override fun info(vararg values: Any?) {
        val message = createMessage(*values).toString()
        addMemoryMessage(message)
        if (Fabric.isInitialized())
            Crashlytics.log(INFO, application.name, message)
        else i(application.name, message)
    }

    override fun debug(vararg values: Any?) {
        val message = createMessage(*values).toString()
        addMemoryMessage(message)
        if (Fabric.isInitialized())
            Crashlytics.log(DEBUG, application.name, message)
        else d(application.name, message)
    }

    override fun logString(): String {
        return logText.toString()
    }

    override fun warn(vararg values: Any?) {
        val message = createMessage(*values).toString()
        addMemoryMessage("Warn: $message")
        if (Fabric.isInitialized())
            Crashlytics.log(WARN, application.name, message)
        else w(application.name, message)
    }

    override fun warn(e: Throwable, vararg values: Any?) {
        val message = createMessage(*values)
        addMemoryMessage(message.addSpace().add(getStackTraceString(e)))
        if (Fabric.isInitialized())
            Crashlytics.log(WARN, application.name, message.toString())
        else w(application.name, message.toString())
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
