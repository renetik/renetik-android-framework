package renetik.android.core.logging

import renetik.android.core.logging.CSLogLevel.Debug
import renetik.android.core.logging.CSLogLevel.Error
import renetik.android.core.logging.CSLogLevel.Info
import renetik.android.core.logging.CSLogLevel.Warn

class CSDummyLogger(
    override val level: CSLogLevel = Debug,
    val listener: ((event: CSLogLevel, message: String?) -> Unit)? = null
) : CSLogger {
    override fun verbose(e: Throwable?, message: String?) {
        listener?.invoke(Debug, message)
    }

    override fun debug(e: Throwable?, message: String?) {
        listener?.invoke(Debug, message)
    }

    override fun info(e: Throwable?, message: String?) {
        listener?.invoke(Info, message)
    }

    override fun warn(e: Throwable?, message: String?) {
        listener?.invoke(Warn, message)
    }

    override fun error(e: Throwable?, message: String?) {
        listener?.invoke(Error, message)
    }
}