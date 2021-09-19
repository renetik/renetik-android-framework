package renetik.java.util

import renetik.android.framework.common.catchAllError
import renetik.android.framework.util.CSHandler.post
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.Executors.newSingleThreadScheduledExecutor
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask

object CSTimer {
    val executor = newSingleThreadScheduledExecutor()

//    private val timer = Timer()

    fun scheduleAtFixedRateRunOnUI(delay: Long = 0, period: Long, function: () -> Unit) =
        scheduleAtFixedRate(delay, period) { post(function) }

    fun scheduleAtFixedRate(delay: Long = 0, period: Long, function: () -> Unit) =
        executor.scheduleAtFixedRate(function, delay, period, TimeUnit.MILLISECONDS)
//    timer.scheduleAtFixedRate(delay, period, function)

    fun scheduleRunOnUI(delay: Long = 0, function: () -> Unit) =
        schedule(delay) { post(function) }

    fun schedule(delay: Long = 0, function: () -> Unit) =
        executor.schedule(function, delay, TimeUnit.MILLISECONDS)

    fun scheduleNano(delay: Long = 0, function: () -> Unit) =
        executor.schedule(function, delay, TimeUnit.NANOSECONDS)
//        executor.scheduleAtFixedRate(function, period, delay, TimeUnit.MILLISECONDS)
//        timer.schedule(delay, function)
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