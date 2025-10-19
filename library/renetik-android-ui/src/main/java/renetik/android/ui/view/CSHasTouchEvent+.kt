package renetik.android.ui.view

import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.action
import renetik.android.event.registration.invoke
import renetik.android.event.registration.launchRepeat
import renetik.android.event.registration.paused
import renetik.android.event.registration.plus
import renetik.android.ui.extensions.view.enabledChange
import renetik.android.ui.extensions.view.onLongClick
import renetik.android.ui.extensions.view.pressed
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.milliseconds

inline fun <T : CSHasTouchEvent> T.onTouch(
    crossinline onTouch: (isDown: Boolean) -> Unit,
): CSRegistration {
    val touchRegistration = eventOnTouch.listen {
        when (it.actionMasked) {
            ACTION_DOWN -> it.consume().also {
                self.pressed(true)
                onTouch(true)
            }

            ACTION_UP, ACTION_CANCEL -> it.consume().also {
                self.pressed(false)
                onTouch(false)
            }

            ACTION_MOVE -> it.consume()
        }
    }
    val enabledRegistration = self.enabledChange { isEnabled ->
        if (!isEnabled) {
            onTouch(false)
            self.pressed(false)
        }
    }
    return CSRegistration(touchRegistration, enabledRegistration)
}

inline fun <T : CSHasTouchEvent> T.onTouch(
    crossinline down: (event: MotionEvent) -> Unit = {},
    crossinline move: (event: MotionEvent) -> Unit,
    crossinline up: (event: MotionEvent?) -> Unit,
): CSRegistration {
    val touchRegistration = eventOnTouch.listen {
        when (it.actionMasked) {
            ACTION_DOWN -> it.consume().run { down(it.event) }
            ACTION_UP, ACTION_CANCEL -> it.consume().run { up(it.event) }
            ACTION_MOVE -> it.consume().run { move(it.event) }
        }
    }
    val enabledRegistration = self.enabledChange { isEnabled ->
        if (!isEnabled) up(null)
    }
    return CSRegistration(touchRegistration, enabledRegistration)
}

@JvmName("onTouchDownOrMove")
inline fun <T : CSHasTouchEvent> T.onTouch(
    crossinline downOrMove: (event: MotionEvent) -> Unit,
): CSRegistration = eventOnTouch.listen {
    when (it.actionMasked) {
        ACTION_DOWN -> it.consume().run { downOrMove(it.event) }
        ACTION_UP, ACTION_CANCEL -> it.consume()
        ACTION_MOVE -> it.consume().run { downOrMove(it.event) }
    }
}

inline fun <T : CSHasTouchEvent> T.onTouchDown(
    parent: CSHasRegistrations,
    crossinline down: () -> Unit,
) = apply { parent + onTouchDown(down) }

inline fun <T : CSHasTouchEvent> T.onTouchDown(
    crossinline down: () -> Unit,
): CSRegistration = onTouch(onTouch = { isDown -> if (isDown) down() })

inline fun <T : CSHasTouchEvent> T.onTouchUp(
    crossinline up: () -> Unit,
): CSRegistration = onTouch(onTouch = { isDown -> if (!isDown) up() })

inline fun <T : CSHasTouchEvent> T.onLongTouch(
    crossinline onTouch: (isDown: Boolean) -> Unit,
    crossinline onClick: () -> Unit,
): CSRegistration {
    var isLongTouch = false
    self.onLongClick {
        isLongTouch = true
        onTouch(true)
    }
    return eventOnTouch.listen {
        when (it.actionMasked) {
            ACTION_UP -> {
                if (isLongTouch) {
                    isLongTouch = false
                    onTouch(false)
                } else onClick()
            }

            ACTION_CANCEL -> {
                if (isLongTouch) {
                    isLongTouch = false
                    onTouch(false)
                }
            }
        }
    }
}

inline fun <T : CSHasTouchEvent> T.onTouch(
    crossinline down: () -> Unit,
    crossinline up: () -> Unit,
): CSRegistration = onTouch(onTouch = { isDown ->
    if (isDown) down() else up()
})

fun <T : CSHasTouchEvent> T.touchToggleActiveIf(
    property: CSProperty<Boolean>
): CSRegistration {
    setToggleActive(property.value)
    val propertyOnChange = property.onChange { setToggleActive(property.value) }
    val registration = onTouchActiveToggle { on -> propertyOnChange.paused { property.value(on) } }
    return CSRegistration(propertyOnChange, registration)
}

fun <T : CSHasTouchEvent> T.touchToggleSelected(
    property: CSProperty<Boolean>
): CSRegistration {
    val propertyOnChange = property.action { setToggleSelected(property.value) }
    val registration =
        onTouchSelectedToggle { on -> propertyOnChange.paused { property.value(on) } }
    return CSRegistration(propertyOnChange, registration)
}

fun <T : CSHasTouchEvent> T.setToggleActive(pressed: Boolean) = apply {
    self.isActivated = pressed
}

fun <T : CSHasTouchEvent> T.setToggleSelected(pressed: Boolean) = apply {
    self.isSelected = pressed
}

inline fun <T : CSHasTouchEvent> T.onTouchActiveToggle(
    crossinline function: (Boolean) -> Unit,
): CSRegistration = onTouch(onTouch = { isDown ->
    if (isDown) {
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
})

inline fun <T : CSHasTouchEvent> T.onTouchSelectedToggle(
    crossinline function: (Boolean) -> Unit,
): CSRegistration = onTouch(onTouch = { isDown ->
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
})

fun CSHasTouchEvent.onTouch(
    parent: CSHasRegistrations,
    delay: Duration = ZERO,
    period: Duration = 250.milliseconds,
    repeat: suspend () -> Unit,
): CSRegistration = onTouch(
    parent,
    delay.inWholeMilliseconds.toInt(),
    period.inWholeMilliseconds.toInt(),
    repeat
)

fun CSHasTouchEvent.onTouch(
    parent: CSHasRegistrations,
    delay: Int, period: Int,
    repeat: suspend () -> Unit,
): CSRegistration {
    var repeatRegistration: CSRegistration? = null
    val touchRegistration = onTouch(down = {
        repeatRegistration?.cancel()
        repeatRegistration = parent.launchRepeat(
            after = delay, period = period, function = repeat
        )
    }, up = {
        repeatRegistration?.cancel()
    })
    return CSRegistration(repeatRegistration, touchRegistration)
}

fun <T> CSHasTouchEvent.onTouch(
    parent: CSHasRegistrations,
    repeat: (step: T) -> Unit,
    step: (repeatCount: Int) -> T,
    delay: Int, period: Int,
    until: (step: T) -> Boolean,
    onDone: () -> Unit,
): CSRegistration {
    var repeatRegistration: CSRegistration? = null
    val registration = onTouch(down = {
        var repeatCount = 0
        val stepValue = step(repeatCount)
        if (!until(stepValue)) onDone() else {
            repeat(stepValue)
            repeatCount++
            repeatRegistration?.cancel()
            repeatRegistration = parent.launchRepeat(delay, period, function = {
                if (!until(step(repeatCount))) {
                    repeatRegistration?.cancel()
                    onDone()
                } else {
                    repeat(step(repeatCount))
                    repeatCount++
                }
            })
        }
    }, up = {
        repeatRegistration?.cancel()
    })
    return CSRegistration(repeatRegistration, registration)
}
