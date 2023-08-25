package renetik.android.ui.extensions.view

import android.view.View
import renetik.android.core.lang.CSHandler.main
import renetik.android.core.lang.send
import renetik.android.core.math.CSPoint
import renetik.android.core.math.CSPoint.Companion.point

val <T : View> T.center: CSPoint<Int>
    get() = point(width / 2, height / 2)

val <T : View> T.centerFloat: CSPoint<Float>
    get() = point(width / 2f, height / 2f)

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
    fun updateLocation() = main.send(delay) {
        if (isAttachedToWindow) getLocationOnScreen(location)
    }
    updateLocation()
    onBoundsChange { updateLocation() }
    return location
}
