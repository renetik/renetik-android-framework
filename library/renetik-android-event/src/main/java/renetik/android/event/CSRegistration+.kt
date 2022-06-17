package renetik.android.event

import renetik.android.core.lang.Func
import java.io.Closeable

fun CSRegistration.pause(): Closeable {
    isActive = false
    return Closeable { resume() }
}

fun CSRegistration.pause(function: Func) {
    isActive = false
    function()
    isActive = true
}

fun CSRegistration.resume() = apply {
    isActive = true
}