package renetik.java.util

import renetik.android.framework.common.catchAllError
import renetik.android.framework.util.CSHandler.post
import java.util.*
import kotlin.concurrent.timerTask

object CSTimer {
    private val timer = Timer()

    fun scheduleAtFixedRateRunOnUI(delay: Long = 0, period: Long, function: () -> Unit) =
        scheduleAtFixedRate(delay, period) { post(function) }

    fun scheduleAtFixedRate(delay: Long = 0, period: Long, function: () -> Unit) =
        timer.scheduleAtFixedRate(delay, period, function)

    fun scheduleRunOnUI(delay: Long = 0, function: () -> Unit) =
        schedule(delay) { post(function) }

    fun schedule(delay: Long = 0, function: () -> Unit) =
        timer.schedule(delay, function)
}

fun Timer.scheduleAtFixedRateRunOnUI(delay: Long = 0, period: Long, function: () -> Unit) =
    scheduleAtFixedRate(delay, period) { post(function) }

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