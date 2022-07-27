import android.view.MotionEvent.*
import renetik.android.core.kotlin.primitives.isTrue
import renetik.android.event.property.CSProperty
import renetik.android.event.property.action
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.paused
import renetik.android.ui.view.CSHasTouchEvent

inline fun <T : CSHasTouchEvent> T.onTouch(
    crossinline function: (down: Boolean) -> Unit) = apply {
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
    crossinline function: () -> Unit) = apply {
    onTouch { down -> if (down) function() }
}

fun <T : CSHasTouchEvent> T.toggleActiveIf(property: CSProperty<Boolean>): CSRegistration {
    setToggleActive(property.value)
    val propertyOnChange = property.onChange { setToggleActive(property.value) }
    onTouchActiveToggle { on -> propertyOnChange.paused { property.value(on) } }
    return propertyOnChange
}

fun <T : CSHasTouchEvent> T.toggleSelectedIf(property: CSProperty<Boolean>): CSRegistration {
    val propertyOnChange = property.action { setToggleSelected(property.value) }
    onTouchSelectedToggle { on ->
        propertyOnChange.paused {
            property.value(on)
        }
    }
    return propertyOnChange
}

fun <T : CSHasTouchEvent> T.setToggleActive(pressed: Boolean) = apply {
    self.isActivated = pressed
}

fun <T : CSHasTouchEvent> T.setToggleSelected(pressed: Boolean) = apply {
    self.isSelected = pressed
}

inline fun <T : CSHasTouchEvent> T.onTouchActiveToggle(
    crossinline function: (Boolean) -> Unit) = onTouch {
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
    crossinline function: (Boolean) -> Unit) = onTouch {
    if (it.isTrue) {
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