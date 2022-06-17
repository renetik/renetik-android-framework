package renetik.android.core.java

import renetik.android.core.lang.CSTimeConstants.MilliToNanoSecondMultiplier
import renetik.android.core.lang.CSTimeConstants.Second
import renetik.android.core.logging.CSLog.logDebug
import java.lang.System.nanoTime

fun measureTimeElapsed(function: () -> Unit) {
    val time = nanoTime()
    function()
    val duration = nanoTime() - time
    logDebug { "Duration ${duration / MilliToNanoSecondMultiplier / Second} Seconds" }
}