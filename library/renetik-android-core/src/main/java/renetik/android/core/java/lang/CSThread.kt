package renetik.android.core.java.lang

import java.lang.Thread.sleep

object CSThread {
    fun sleepNano(nanoseconds: Long) {
        val millis = nanoseconds / 1_000_000
        val nanos = (nanoseconds % 1_000_000).toInt()
        sleep(millis, nanos)
    }
}