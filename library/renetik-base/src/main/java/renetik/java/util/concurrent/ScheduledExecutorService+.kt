package renetik.java.util.concurrent

import renetik.android.framework.common.catchAllError
import renetik.android.framework.util.CSHandler.post
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.NANOSECONDS

fun ScheduledExecutorService.background(delay: Long = 0, function: () -> Unit) =
    schedule({ catchAllError { function() } }, delay, MILLISECONDS)

fun ScheduledExecutorService.backgroundNano(delay: Long = 0, function: () -> Unit) =
    schedule({ catchAllError { function() } }, delay, NANOSECONDS)

fun ScheduledExecutorService.backgroundRunOnUi(delay: Long = 0, function: () -> Unit) =
    background(delay) { post(function) }

fun ScheduledExecutorService.backgroundRepeat(delay: Long = 0,
                                              period: Long, function: () -> Unit) =
    scheduleAtFixedRate({ catchAllError { function() } }, delay, period, MILLISECONDS)

fun ScheduledExecutorService.backgroundRepeatNano(delay: Long = 0,
                                              period: Long, function: () -> Unit) =
    scheduleAtFixedRate({ catchAllError { function() } }, delay, period, NANOSECONDS)

fun ScheduledExecutorService.backgroundRepeatRunOnUI(delay: Long = 0,
                                                     period: Long, function: () -> Unit) =
    backgroundRepeat(delay, period) { post(function) }

fun ScheduledExecutorService.backgroundRepeat(period: Long, function: () -> Unit) =
    backgroundRepeat(delay = period, period = period, function = function)