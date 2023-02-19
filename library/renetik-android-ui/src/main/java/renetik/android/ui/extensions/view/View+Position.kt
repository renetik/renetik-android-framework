package renetik.android.ui.extensions.view

import android.view.View
import renetik.android.core.math.CSPoint
import renetik.android.event.util.CSLater.later

val <T : View> T.topFromBottom get() = superview?.let { it.height - top } ?: height

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

fun View.registerToLocationOnScreen(delay: Int = 0): IntArray {
    val location = IntArray(2)
    fun updateLocation() =
        later(delay) { if (isAttachedToWindow) getLocationOnScreen(location) }
    updateLocation()
    onSizeChange { updateLocation() }
    return location
}
