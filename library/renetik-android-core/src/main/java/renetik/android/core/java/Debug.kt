package renetik.android.core.java

import renetik.android.core.logging.CSLog.logDebug
import java.lang.System.nanoTime
import kotlin.time.Duration.Companion.nanoseconds

fun measureTimeElapsed(function: () -> Unit) {
    val startTimeNano = nanoTime()
    function()
    val durationNano = nanoTime() - startTimeNano
    logDebug { "Duration ${durationNano.nanoseconds.inWholeSeconds} Seconds" }
}