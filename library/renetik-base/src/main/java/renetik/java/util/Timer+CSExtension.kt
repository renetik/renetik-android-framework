package renetik.java.util

import renetik.android.framework.common.catchAllError
import renetik.android.framework.util.CSMainHandler.postOnMain
import renetik.java.util.concurrent.*
import java.util.*
import java.util.concurrent.Executors.newSingleThreadScheduledExecutor
import java.util.concurrent.ScheduledExecutorService
import kotlin.concurrent.timerTask

object CSTimer {

    val executor: ScheduledExecutorService = newSingleThreadScheduledExecutor()

    fun scheduleAtFixedRateRunOnUI(delay: Long = 0, period: Long, function: () -> Unit) =
        executor.backgroundRepeatRunOnUI(delay, period, function)

    fun scheduleAtFixedRate(delay: Long = 0, period: Long, function: () -> Unit) =
        executor.backgroundRepeat(delay, period, function)

    fun scheduleRunOnUI(delay: Long = 0, function: () -> Unit) =
        executor.backgroundRunOnUi(delay, function)

    fun schedule(delay: Long = 0, function: () -> Unit) =
        executor.background(delay, function)

    fun scheduleNano(delay: Long = 0, function: () -> Unit) =
        executor.looperBackgroundNano(delay, function)
}

fun Timer.scheduleAtFixedRateRunOnUI(delay: Long = 0, period: Long, function: () -> Unit) =
    scheduleAtFixedRate(delay, period) { postOnMain(function) }

inline fun Timer.scheduleAtFixedRate(delay: Long = 0, period: Long,
                                     crossinline function: () -> Unit): TimerTask {
    val task = timerTask { catchAllError { function() } }
    scheduleAtFixedRate(task, delay, period)
    return task
}

inline fun Timer.schedule(delay: Long = 0,
                          crossinline function: () -> Unit): TimerTask {
    val task = timerTask { catchAllError { function() } }
    schedule(task, delay)
    return task
}

