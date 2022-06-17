package renetik.android.ui.view

import android.view.MotionEvent
import android.view.View

interface CSHasTouchEvent {
    val self: View
    var onTouchEvent: ((event: MotionEvent) -> Boolean)?
}