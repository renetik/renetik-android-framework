package renetik.android.view

import android.view.View
import renetik.android.framework.math.CSPoint

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
