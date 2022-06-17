package renetik.android.extensions

import android.view.MotionEvent
import android.view.View

fun MotionEvent.isInside(view: View) =
    (view.x <= x && x < view.width) && (view.y <= y && x < view.height)