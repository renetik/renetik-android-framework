package renetik.android.framework.event

import renetik.android.framework.Func
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