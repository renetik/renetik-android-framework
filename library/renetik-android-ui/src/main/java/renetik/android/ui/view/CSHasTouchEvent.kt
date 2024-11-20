package renetik.android.ui.view

import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import renetik.android.event.registration.CSRegistration

interface CSHasTouchEvent {
    val self: View
    var onTouchEvent: ((event: MotionEvent) -> Boolean)?
}

interface CSHasDrawEvent {
    val self: View
    fun listenOnDraw(listener: (Canvas) -> Unit): CSRegistration
}