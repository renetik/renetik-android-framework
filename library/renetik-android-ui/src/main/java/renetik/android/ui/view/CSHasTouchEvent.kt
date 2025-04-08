package renetik.android.ui.view

import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import renetik.android.event.CSEvent
import renetik.android.event.registration.CSRegistration

data class CSTouchEventArgs(var event: MotionEvent, var isHandled: Boolean) {
    fun handled() {
        isHandled = true
    }

    val action get() = event.action
    val actionMasked get() = event.actionMasked

    companion object {
        private val instance = CSTouchEventArgs(MotionEvent.obtain(0, 0, 0, 0f, 0f, 0), false)
        fun with(event: MotionEvent) = instance.apply { this.event = event }
        val isHandled: Boolean get() = instance.isHandled
    }
}

interface CSHasTouchEvent {
    val self: View
    val eventOnTouch: CSEvent<CSTouchEventArgs>

    fun processTouchEvent(event: MotionEvent): Boolean {
        if (eventOnTouch.isListened)
            eventOnTouch.fire(CSTouchEventArgs.with(event))
        val handled = eventOnTouch.isListened && CSTouchEventArgs.isHandled
        return handled
    }
}

interface CSHasDrawEvent {
    val self: View
    fun listenOnDraw(listener: (Canvas) -> Unit): CSRegistration
}