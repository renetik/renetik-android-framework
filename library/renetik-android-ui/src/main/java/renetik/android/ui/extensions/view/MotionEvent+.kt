package renetik.android.ui.extensions.view

import android.view.MotionEvent
import android.view.View

fun MotionEvent.isInside(view: View) =
    (view.x <= x && x < view.width) && (view.y <= y && x < view.height)