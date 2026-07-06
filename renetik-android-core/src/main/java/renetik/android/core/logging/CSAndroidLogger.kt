package renetik.android.core.logging

import android.util.Log
import renetik.android.core.BuildConfig.LIBRARY_PACKAGE_NAME
import renetik.android.core.lang.CSEnvironment.isDebug
import renetik.android.core.logging.CSLogLevel.Debug
import renetik.android.core.logging.CSLogLevel.Error
import renetik.android.core.logging.CSLogLevel.Info
import renetik.android.core.logging.CSLogLevel.Verbose
import renetik.android.core.logging.CSLogLevel.Warn

class CSAndroidLogger(
    val tag: String = LIBRARY_PACKAGE_NAME,
    override val level: CSLogLevel = if (isDebug) Debug else Error,
    val listener: CSLogListener? = null
) : CSLogger {

    override fun verbose(e: Throwable?, message: String?) {
        if (isDisabled(Verbose)) return
        Log.d(tag, message, e)
        listener?.invoke(Debug, message, e)
    }

    override fun debug(e: Throwable?, message: String?) {
        if (isDisabled(Debug)) return
        Log.d(tag, message, e)
        listener?.invoke(Debug, message, e)
    }

    override fun info(e: Throwable?, message: String?) {
        if (isDisabled(Info)) return
        Log.i(tag, message, e)
        listener?.invoke(Info, message, e)
    }

    override fun warn(e: Throwable?, message: String?) {
        if (isDisabled(Warn)) return
        Log.w(tag, message, e)
        listener?.invoke(Warn, message, e)
    }

    override fun error(e: Throwable?, message: String?) {
        if (isDisabled(Error)) return
        Log.e(tag, message, e)
        listener?.invoke(Error, message, e)
    }


}
