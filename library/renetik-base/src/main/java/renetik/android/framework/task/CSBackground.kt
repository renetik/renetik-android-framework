package renetik.android.framework.task

import renetik.java.util.concurrent.background
import renetik.java.util.concurrent.looperBackgroundNano
import renetik.java.util.concurrent.backgroundRepeat
import java.util.concurrent.Executors.newSingleThreadScheduledExecutor

object CSBackground {
    private val executor = newSingleThreadScheduledExecutor()

    fun background(function: () -> Unit) =
        executor.background(function = function)

    fun background(delay: Long = 0, function: () -> Unit) =
        executor.background(delay = delay, function = function)

    fun backgroundNano(delay: Long = 0, function: () -> Unit) =
        executor.looperBackgroundNano(delay = delay, function = function)

    fun background(delay: Int = 0, function: () -> Unit) =
        executor.background(delay = delay.toLong(), function = function)

    fun backgroundRepeat(delay: Long, period: Long, function: () -> Unit) =
        executor.backgroundRepeat(delay = delay, period = period, function = function)

    fun backgroundRepeat(period: Long, function: () -> Unit) =
        executor.backgroundRepeat(period, function)
}

