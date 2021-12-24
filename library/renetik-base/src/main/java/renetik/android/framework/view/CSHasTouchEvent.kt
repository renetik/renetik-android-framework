package renetik.android.framework.view

import android.view.MotionEvent
import android.view.View

interface CSHasTouchEvent {
    val self: View
    var onTouchEvent: ((event: MotionEvent) -> Boolean)?
}