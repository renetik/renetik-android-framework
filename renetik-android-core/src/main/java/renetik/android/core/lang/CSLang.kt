package renetik.android.core.lang

import android.os.Process.killProcess
import android.os.Process.myPid
import renetik.android.core.lang.CSLang.ExitStatus.OK
import renetik.android.core.logging.CSLog.logInfo
import kotlin.system.exitProcess
import kotlin.system.measureNanoTime

object CSLang {
    fun <T> measureAndLog(title: String, func: () -> T): T {
        var value by notNull<T>()
        val elapsedMs = measureNanoTime { value = func() } / 1_000_000
        logInfo { "measure $title took $elapsedMs ms" }
        return value
    }

    enum class ExitStatus(val code: Int) {
        OK(0), Error(1)
    }

    fun exit(status: ExitStatus = OK) {
        killProcess(myPid())
        exitProcess(status.code)
    }
}

inline fun <T : Any, R> synchronized(lock: T, block: (T) -> R): R =
    kotlin.synchronized(lock) { block(lock) }