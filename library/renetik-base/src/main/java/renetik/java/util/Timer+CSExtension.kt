package renetik.java.util

import renetik.android.framework.util.CSHandler.post
import java.util.*

object CSTimer {
    private var timer = Timer()

    fun scheduleAtFixedRateRunOnUI(delay: Long = 0, period: Long, function: () -> Unit) =
        scheduleAtFixedRate(delay, period) { post(function) }

    fun scheduleAtFixedRate(delay: Long = 0, period: Long, function: () -> Unit) =
        timer.scheduleAtFixedRate(delay, period, function)

    fun scheduleRunOnUI(delay: Long, function: () -> Unit) =
        schedule(delay) { post(function) }

    fun schedule(delay: Long, function: () -> Unit) =
        timer.schedule(delay, function)

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

fun Timer.schedule(delay: Long = 0, function: () -> Unit): TimerTask {
    val task = object : TimerTask() {
        override fun run() {
            function()
        }
    }
    schedule(task, delay)
    return task
}