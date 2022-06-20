package renetik.android.core.logging

import android.util.Log
import android.util.Log.getStackTraceString
import renetik.android.core.BuildConfig.LIBRARY_PACKAGE_NAME
import renetik.android.core.kotlin.text.add
import renetik.android.core.kotlin.text.addSpace
import renetik.android.core.lang.CSEnvironment.isDebug
import renetik.android.core.lang.void
import renetik.android.core.logging.CSLoggerEvent.*

class CSAndroidLogger(val name: String = LIBRARY_PACKAGE_NAME,
                      val listener: ((event: CSLoggerEvent,
                                      message: String) -> void)? = null)
    : CSLogger {

    override fun error(vararg values: Any?) {
        val message = createMessage(*values).toString()
        Log.e(name, message)
        listener?.invoke(Error, message)
    }

    override fun error(e: Throwable, vararg values: Any?) {
        val message = createMessage(*values)
        Log.e(name, message.toString(), e)
        listener?.invoke(Error, message.addSpace().add(getStackTraceString(e)).toString())
    }

    override fun info(vararg values: Any?) {
        val message = createMessage(*values).toString()
        Log.i(name, message)
        listener?.invoke(Info, message)
    }

    override fun debug(vararg values: Any?) {
        if (!isDebug) return
        val message = createMessage(*values).toString()
        Log.d(name, message)
        listener?.invoke(Debug, message)
    }

    override fun debug(e: Throwable, vararg values: Any?) {
        if (!isDebug) return
        val message = createMessage(*values)
        Log.d(name, message.toString(), e)
        listener?.invoke(Debug, message.addSpace().add(getStackTraceString(e)).toString())
    }

    override fun warn(vararg values: Any?) {
        val message = createMessage(*values).toString()
        Log.w(name, message)
        listener?.invoke(Warn, message)
    }

    override fun warn(e: Throwable, vararg values: Any?) {
        val message = createMessage(*values)
        Log.w(name, message.toString(), e)
        listener?.invoke(Warn, message.addSpace().add(getStackTraceString(e)).toString())
    }

    private fun createMessage(vararg values: Any?) = StringBuilder().apply {
        values.forEach { it?.let { add(it).addSpace() } }
    }
}
