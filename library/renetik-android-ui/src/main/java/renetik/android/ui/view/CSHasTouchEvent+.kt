package renetik.android.ui.view

import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import renetik.android.core.kotlin.primitives.isTrue
import renetik.android.event.property.CSProperty
import renetik.android.event.property.action
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.cancel
import renetik.android.event.registration.paused
import renetik.android.event.registration.registerRepeat

inline fun <T : CSHasTouchEvent> T.onTouch(
    crossinline function: (down: Boolean) -> Unit
) = apply {
    onTouchEvent = {
        when (it.actionMasked) {
            ACTION_DOWN -> true.also {
                self.isPressed = true
                function(true)
            }
            ACTION_UP, ACTION_CANCEL -> true.also {
                self.isPressed = false
                function(false)
            }
            ACTION_MOVE -> true
            else -> false
        }
    }
}

inline fun <T : CSHasTouchEvent> T.onTouchDown(
    crossinline function: () -> Unit
) = apply {
    onTouch { down -> if (down) function() }
}


inline fun <T : CSHasTouchEvent> T.onTouchUp(
    crossinline function: () -> Unit
) = apply {
    onTouch { down -> if (!down) function() }
}

inline fun <T : CSHasTouchEvent> T.onTouch(
    crossinline down: () -> Unit,
    crossinline up: () -> Unit
) = apply {
    onTouch { down -> if (down) down() else up() }
}

fun <T : CSHasTouchEvent> T.toggleActiveIf(property: CSProperty<Boolean>): CSRegistration {
    setToggleActive(property.value)
    val propertyOnChange = property.onChange { setToggleActive(property.value) }
    onTouchActiveToggle { on -> propertyOnChange.paused { property.value(on) } }
    return propertyOnChange
}

fun <T : CSHasTouchEvent> T.toggleSelectedIf(property: CSProperty<Boolean>): CSRegistration {
    val propertyOnChange = property.action { setToggleSelected(property.value) }
    onTouchSelectedToggle { on -> propertyOnChange.paused { property.value(on) } }
    return propertyOnChange
}

fun <T : CSHasTouchEvent> T.setToggleActive(pressed: Boolean) = apply {
    self.isActivated = pressed
}

fun <T : CSHasTouchEvent> T.setToggleSelected(pressed: Boolean) = apply {
    self.isSelected = pressed
}

inline fun <T : CSHasTouchEvent> T.onTouchActiveToggle(
    crossinline function: (Boolean) -> Unit
) = onTouch {
    if (it.isTrue) {
        if (!self.isActivated) {
            function(true)
            self.isSelected = true
            self.isActivated = true
        } else if (self.isActivated) {
            self.isPressed = false
            self.isSelected = true
            self.isActivated = false
        }
    } else {
        if (self.isActivated) {
            self.isSelected = false
        } else {
            function(false)
            self.isActivated = false
            self.isSelected = false
        }
    }
}

inline fun <T : CSHasTouchEvent> T.onTouchSelectedToggle(
    crossinline function: (Boolean) -> Unit
) = onTouch { isDown ->
    if (isDown) {
        if (!self.isSelected) {
            function(true)
            self.isActivated = true
            self.isSelected = true
        } else if (self.isSelected) {
            self.isPressed = true
            self.isActivated = true
            self.isSelected = false
        }
    } else {
        if (self.isSelected) {
            self.isActivated = false
        } else {
            function(false)
            self.isSelected = false
            self.isActivated = false
        }
    }
}

fun <T> CSHasTouchEvent.onTouch(
    parent: CSHasRegistrations,
    repeat: (step: T) -> Unit,
    step: (repeatCount: Int) -> T,
    after: Int, interval: Int,
    until: (step: T) -> Boolean,
    onDone: () -> Unit
) {
    var repeatCount: Int
    var repeatRegistration: CSRegistration? = null
    onTouch(down = {
        repeatCount = 0
        repeat(step(repeatCount))
        parent.cancel(repeatRegistration)
        if (self.isEnabled) repeatRegistration = parent.registerRepeat(
            interval, after, function = {
                repeat(step(repeatCount))
                repeatCount++
                if (!until(step(repeatCount))) {
                    parent.cancel(repeatRegistration)
                    onDone()
                }
            }
        )
    }, up = {
        parent.cancel(repeatRegistration)
    })
}
