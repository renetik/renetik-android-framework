package renetik.android.ui.extensions.view

import android.view.MotionEvent
import android.view.View

fun MotionEvent.isInside(view: View) =
    (view.x <= x && x < view.width) && (view.y <= y && x < view.height)

fun MotionEvent.offsetLocation(location: IntArray) {
    offsetLocation(location[0].toFloat(), location[1].toFloat())
}