package renetik.android.core.java.util.concurrent

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import renetik.android.core.lang.CSMainHandler.postOnMain
import renetik.android.core.lang.catchAllError
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.NANOSECONDS

inline fun ScheduledExecutorService.background(
    delay: Long = 0, @WorkerThread crossinline function: () -> Unit) =
    schedule({ catchAllError { function() } }, delay, MILLISECONDS)!!

inline fun ScheduledExecutorService.backgroundNano(
    delay: Long = 0, @WorkerThread crossinline function: () -> Unit) =
    schedule({ catchAllError { function() } }, delay, NANOSECONDS)!!

fun ScheduledExecutorService.backgroundRunOnUi(
    delay: Long = 0, @UiThread function: () -> Unit) =
    background(delay) { postOnMain(function) }

inline fun ScheduledExecutorService.backgroundRepeat(
    delay: Long = 0, period: Long, @WorkerThread crossinline function: () -> Unit) =
    scheduleAtFixedRate({ catchAllError { function() } }, delay, period, MILLISECONDS)!!

inline fun ScheduledExecutorService.backgroundRepeatNano(
    delay: Long = 0, period: Long, @WorkerThread crossinline function: () -> Unit) =
    scheduleAtFixedRate({ catchAllError { function() } }, delay, period, NANOSECONDS)

fun ScheduledExecutorService.backgroundRepeatRunOnUI(
    delay: Long = 0, period: Long, @UiThread function: () -> Unit) =
    backgroundRepeat(delay, period) { postOnMain(function) }

inline fun ScheduledExecutorService.backgroundRepeat(
    period: Long, @WorkerThread crossinline function: () -> Unit) =
    backgroundRepeat(delay = period, period = period, function = function)

inline fun ScheduledExecutorService.backgroundRepeatNano(
    period: Long, @WorkerThread crossinline function: () -> Unit) =
    backgroundRepeatNano(delay = period, period = period, function = function)