package renetik.android.ui.extensions.view

import android.view.View
import renetik.android.core.math.CSPoint
import renetik.android.event.util.CSLater.later

val <T : View> T.topFromBottom get() = superview?.let { it.height - top } ?: height

var <T : View> T.topFloat
    get() = y
    set(value) {
        y = value
    }
var <T : View> T.bottomFloat
    get() = y + height
    set(value) {
        y = value - height
    }
var <T : View> T.leftFloat
    get() = x
    set(value) {
        x = value
    }
var <T : View> T.rightFloat
    get() = x + width
    set(value) {
        x = value - width
    }

val View.locationOnScreen: CSPoint<Int>
    get() {
        val location = IntArray(2)
        getLocationOnScreen(location)
        return CSPoint(location[0], location[1])
    }

val View.locationInWindow: CSPoint<Int>
    get() {
        val location = IntArray(2)
        getLocationInWindow(location)
        return CSPoint(location[0], location[1])
    }

fun View.updatedLocationOnScreen(delay: Int = 0): IntArray {
    val location = IntArray(2)
    fun updateLocation() = later(delay) {
        if (isAttachedToWindow) getLocationOnScreen(location)
    }
    updateLocation()
    onBoundsChange { updateLocation() }
    return location
}
