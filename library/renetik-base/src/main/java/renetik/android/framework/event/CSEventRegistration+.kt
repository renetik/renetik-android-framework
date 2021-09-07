package renetik.android.framework.event

import java.io.Closeable

fun CSEventRegistration.pause(): Closeable {
    isActive = false
    return Closeable { resume() }
}

fun CSEventRegistration.resume() = apply {
    isActive = true
}