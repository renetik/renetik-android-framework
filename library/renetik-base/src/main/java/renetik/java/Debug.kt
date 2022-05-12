package renetik.java

import renetik.android.framework.lang.CSTimeConstants.MilliToNanoSecondMultiplier
import renetik.android.framework.lang.CSTimeConstants.Second
import renetik.android.framework.logging.CSLog.debug
import java.lang.System.nanoTime

fun measureTimeElapsed(function: () -> Unit) {
    val time = nanoTime()
    function()
    val duration = nanoTime() - time
    debug { "Duration ${duration / MilliToNanoSecondMultiplier / Second} Seconds" }
}