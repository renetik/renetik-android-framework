package renetik.java

import renetik.android.framework.lang.CSTimeConstants.MilliToNanoSecondMultiplier
import renetik.android.framework.lang.CSTimeConstants.Second
import renetik.android.framework.logging.CSLog.logDebug
import java.lang.System.nanoTime

fun measureTimeElapsed(function: () -> Unit) {
    val time = nanoTime()
    function()
    val duration = nanoTime() - time
    logDebug { "Duration ${duration / MilliToNanoSecondMultiplier / Second} Seconds" }
}