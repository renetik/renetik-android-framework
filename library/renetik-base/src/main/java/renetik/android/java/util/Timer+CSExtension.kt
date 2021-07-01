package renetik.android.java.util

import renetik.android.os.CSHandler.post
import java.util.*

object CSTimer {
    fun scheduleAtFixedRateRunOnUI(delay: Long = 0, period: Long, function: () -> Unit) =
        scheduleAtFixedRate(delay, period) { post(function) }

    fun scheduleAtFixedRate(delay: Long = 0, period: Long, function: () -> Unit) = Timer().apply {
        scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                function()
            }
        }, delay, period)
    }
}