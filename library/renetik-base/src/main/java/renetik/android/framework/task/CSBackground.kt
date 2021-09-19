package renetik.android.framework.task

import renetik.java.util.CSTimer.schedule
import renetik.java.util.CSTimer.scheduleAtFixedRate
import renetik.java.util.CSTimer.scheduleNano

object CSBackground {

    fun background(function: () -> Unit) =
        schedule(function = function)
//    fun background(function: () -> Unit) = backgroundHandler.post(function)

//    fun background(delay: Int = 0, function: () -> Unit) =
//        backgroundHandler.postDelayed(function, delay.toLong())

    fun background(delay: Long = 0, function: () -> Unit) =
        schedule(delay = delay, function = function)

    fun backgroundNano(delay: Long = 0, function: () -> Unit) =
        scheduleNano(delay = delay, function = function)

    fun background(delay: Int = 0, function: () -> Unit) =
        schedule(delay = delay.toLong(), function = function)

    fun backgroundRepeat(delay: Long, period: Long, function: () -> Unit) =
        scheduleAtFixedRate(delay = delay, period = period, function = function)

    fun backgroundRepeat(period: Long, function: () -> Unit) =
        scheduleAtFixedRate(delay = period, period = period, function = function)


//    val backgroundHandler: Handler by lazy { Handler(backgroundThread.looper) }
//
//    val backgroundThread: HandlerThread by lazy {
//        HandlerThread("CSBackground HandlerThread").apply {
//            setUncaughtExceptionHandler { _, e -> later { throw RuntimeException(e) } }
//            start()
//        }
//    }
}

