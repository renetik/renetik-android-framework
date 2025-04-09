package renetik.android.ui.view

import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import renetik.android.core.kotlin.then
import renetik.android.event.CSEvent
import renetik.android.event.registration.CSRegistration

data class CSTouchEventArgs(val event: MotionEvent, var isHandled: Boolean = false) {
    fun handled() = then { isHandled = true }
    val action get() = event.action
    val actionMasked get() = event.actionMasked
}

interface CSHasTouchEvent {
    val self: View
    val eventOnTouch: CSEvent<CSTouchEventArgs>

    fun processTouchEvent(event: MotionEvent): Boolean {
        if (eventOnTouch.isListened) {
            val args = CSTouchEventArgs(event)
            eventOnTouch.fire(args)
            return args.isHandled
        }
        return false
    }
}

interface CSHasDrawEvent {
    val self: View
    fun listenOnDraw(listener: (Canvas) -> Unit): CSRegistration
}