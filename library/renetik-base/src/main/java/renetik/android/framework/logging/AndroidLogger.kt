package renetik.android.framework.logging

import android.util.Log
import android.util.Log.getStackTraceString
import renetik.android.framework.CSApplication.Companion.app
import renetik.android.framework.CSContext
import renetik.android.framework.logging.CSLoggerEvent.*
import renetik.kotlin.text.add
import renetik.kotlin.text.addSpace

class AndroidLogger() : CSContext(), CSLogger {

    var listener: CSLoggerListener? = null

    constructor(listener: CSLoggerListener) : this() {
        this.listener = listener
    }

    override fun error(vararg values: Any?) {
        val message = createMessage(*values).toString()
        Log.e(app.name, message)
        listener?.onLogEvent(Error, message)
    }

    override fun error(e: Throwable, vararg values: Any?) {
        val message = createMessage(*values)
        Log.e(app.name, message.toString(), e)
        listener?.onLogEvent(Error, message.addSpace().add(getStackTraceString(e)).toString())
    }

    override fun info(vararg values: Any?) {
        val message = createMessage(*values).toString()
        Log.i(app.name, message)
        listener?.onLogEvent(Info, message)
    }

    override fun debug(vararg values: Any?) {
        if (!app.isDebugBuild) return
        val message = createMessage(*values).toString()
        Log.d(app.name, message)
        listener?.onLogEvent(Debug, message)
    }

    override fun debug(e: Throwable, vararg values: Any?) {
        if (!app.isDebugBuild) return
        val message = createMessage(*values)
        Log.d(app.name, message.toString(), e)
        listener?.onLogEvent(Debug, message.addSpace().add(getStackTraceString(e)).toString())
    }

    override fun warn(vararg values: Any?) {
        val message = createMessage(*values).toString()
        Log.w(app.name, message)
        listener?.onLogEvent(Warn, message)
    }

    override fun warn(e: Throwable, vararg values: Any?) {
        val message = createMessage(*values)
        Log.w(app.name, message.toString(), e)
        listener?.onLogEvent(Warn, message.addSpace().add(getStackTraceString(e)).toString())
    }

    private fun createMessage(vararg values: Any?) = StringBuilder().apply {
        values.forEach { it?.let { add(it).addSpace() } }
    }
}
