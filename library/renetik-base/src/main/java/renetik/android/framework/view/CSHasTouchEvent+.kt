import android.view.MotionEvent.*
import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.view.CSHasTouchEvent
import renetik.android.primitives.isTrue

fun <T : CSHasTouchEvent> T.onTouch(function: (Boolean) -> Unit) = apply {
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

//fun <T : CSHasTouchEvent> T.toggleIfTrue(
//    property: CSEventProperty<Boolean>): CSEventRegistration {
//    onTouch {
//        if (it.isTrue) {
//            if (!self.isActivated) property.setTrue()
//        } else {
//
//        }
//    }
//    return self.activatedIf(property) { it.isTrue }
//}

fun <T : CSHasTouchEvent> T.toggleIf(property: CSEventProperty<Boolean>): CSEventRegistration {
    setTogglePressed(property.value)
    val propertyOnChange = property.onChange { setTogglePressed(property.value) }
    onTouchToggle { on -> propertyOnChange.pause().use { property.value(on) } }
    return propertyOnChange
}

fun <T : CSHasTouchEvent> T.setTogglePressed(pressed: Boolean) = apply {
    self.isActivated = pressed
}

fun <T : CSHasTouchEvent> T.onTouchToggle(function: (Boolean) -> Unit) = onTouch {
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