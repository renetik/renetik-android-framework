package renetik.java

import renetik.android.framework.lang.CSTimeConstants.MilliToNanoSecondMultiplier
import renetik.android.framework.lang.CSTimeConstants.Second
import renetik.android.framework.logging.CSLog.info

fun measureTimeElapsed(function: () -> Unit) {
    val time = System.nanoTime()
    function()
    val duration = System.nanoTime() - time
    info("Duration ${duration / MilliToNanoSecondMultiplier / Second} Seconds")
}