import android.view.MotionEvent.*
import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.view.CSHasTouchEvent

fun CSHasTouchEvent.onTouch(function: (Boolean) -> Unit) {
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

fun <T : CSHasTouchEvent> T.toggleIf(property: CSEventProperty<Boolean>): CSEventRegistration {
    setTogglePressed(property.value)
    val propertyOnChange = property.onChange { setTogglePressed(property.value) }
    onTouchToggle { on -> propertyOnChange.pause().use { property.value(on) } }
    return propertyOnChange
}

fun <T : CSHasTouchEvent> T.setTogglePressed(pressed: Boolean) = apply {
    if (pressed) {
        self.isSelected = true
        self.isActivated = true
        self.isPressed = false
    } else {
        self.isSelected = false
        self.isActivated = false
        self.isPressed = false
    }
}

fun <T : CSHasTouchEvent> T.onTouchToggle(function: (Boolean) -> Unit) = apply {
    onTouchEvent = {
        when (it.actionMasked) {
            ACTION_DOWN -> true.also {
                if (!self.isSelected) {
                    function(true)
                    self.isActivated = true
                } else self.isActivated = false
                self.isPressed = true
            }
            ACTION_UP, ACTION_CANCEL -> true.also {
                if (self.isActivated) setTogglePressed(true)
                else {
                    function(false)
                    setTogglePressed(false)
                }
            }
            ACTION_MOVE -> true
            else -> false
        }
    }
}