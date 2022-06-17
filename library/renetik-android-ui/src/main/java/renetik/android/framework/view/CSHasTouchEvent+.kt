import android.view.MotionEvent.*
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.pause
import renetik.android.event.property.CSEventProperty
import renetik.android.framework.view.CSHasTouchEvent
import renetik.android.core.kotlin.primitives.isTrue

fun <T : CSHasTouchEvent> T.onTouch(function: (down: Boolean) -> Unit) = apply {
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

fun <T : CSHasTouchEvent> T.onTouchDown(function: () -> Unit) = apply {
    onTouch { down -> if (down) function() }
}

fun <T : CSHasTouchEvent> T.toggleActiveIf(property: CSEventProperty<Boolean>): CSRegistration {
    setToggleActive(property.value)
    val propertyOnChange = property.onChange { setToggleActive(property.value) }
    onTouchActiveToggle { on -> propertyOnChange.pause().use { property.value(on) } }
    return propertyOnChange
}

fun <T : CSHasTouchEvent> T.toggleSelectedIf(property: CSEventProperty<Boolean>): CSRegistration {
    setToggleSelected(property.value)
    val propertyOnChange = property.onChange { setToggleSelected(property.value) }
    onTouchSelectedToggle { on -> propertyOnChange.pause().use { property.value(on) } }
    return propertyOnChange
}

fun <T : CSHasTouchEvent> T.setToggleActive(pressed: Boolean) = apply {
    self.isActivated = pressed
}

fun <T : CSHasTouchEvent> T.setToggleSelected(pressed: Boolean) = apply {
    self.isSelected = pressed
}

fun <T : CSHasTouchEvent> T.onTouchActiveToggle(function: (Boolean) -> Unit) = onTouch {
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

fun <T : CSHasTouchEvent> T.onTouchSelectedToggle(function: (Boolean) -> Unit) = onTouch {
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