package renetik.android.ui.extensions.view

import android.view.MotionEvent
import android.view.View
import renetik.android.core.math.CSPoint

fun MotionEvent.isInside(view: View) =
    (view.x <= x && x < view.width) && (view.y <= y && x < view.height)

fun MotionEvent.offsetLocation(location: IntArray) =
    offsetLocation(location[0].toFloat(), location[1].toFloat())

fun MotionEvent.clampedPoint(view: View): CSPoint<Float> = CSPoint(
    x.coerceIn(0f, view.width.toFloat()),
    y.coerceIn(0f, view.height.toFloat())
)