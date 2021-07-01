package renetik.android.java.util

import renetik.android.os.CSHandler.post
import java.util.*

object CSTimer {
    private var timer = Timer()

    fun scheduleAtFixedRateRunOnUI(delay: Long = 0, period: Long, function: () -> Unit) =
        scheduleAtFixedRate(delay, period) { post(function) }

    fun scheduleAtFixedRate(delay: Long = 0, period: Long, function: () -> Unit) =
        timer.scheduleAtFixedRate(delay, period, function)

    fun cancel() {
        timer.cancel()
        timer = Timer()
    }
}

fun Timer.scheduleAtFixedRateRunOnUI(delay: Long = 0, period: Long, function: () -> Unit) =
    scheduleAtFixedRate(delay, period) { post(function) }

fun Timer.scheduleAtFixedRate(delay: Long = 0, period: Long, function: () -> Unit): TimerTask {
    val task = object : TimerTask() {
        override fun run() {
            function()
        }
    }
    scheduleAtFixedRate(task, delay, period)
    return task
}