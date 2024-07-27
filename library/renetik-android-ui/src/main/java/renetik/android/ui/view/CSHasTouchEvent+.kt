package renetik.android.ui.view

import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import renetik.android.core.lang.variable.CSWeakVariable.Companion.weak
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.action
import renetik.android.event.registration.laterEach
import renetik.android.event.registration.launch
import renetik.android.event.registration.paused
import renetik.android.ui.extensions.view.onLongClick
import renetik.android.ui.extensions.view.pressed
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

fun <T : CSHasTouchEvent> T.onTouch(
    down: (isDown: Boolean) -> Unit,
) = apply {
    val onDown by weak(down) // To fix leakcanary (false?) report
    onTouchEvent = {
        when (it.actionMasked) {
            ACTION_DOWN -> true.also {
                self.pressed(true)
                onDown?.invoke(true)
            }

            ACTION_UP, ACTION_CANCEL -> true.also {
                self.pressed(false)
                onDown?.invoke(false)
            }

            ACTION_MOVE -> true
            else -> false
        }
    }
}


inline fun <T : CSHasTouchEvent> T.onLongTouch(
    duration: Duration = 2.seconds,
    crossinline down: (isDown: Boolean) -> Unit,
): CSRegistration {
    var registration: CSRegistration? = null
    onTouch(down = { isDown ->
        if (isDown) registration = Main.launch {
            delay(duration)
            if (it.isActive) {
                down(true)
                registration = null
            }
        }
        else registration?.cancel() ?: down(false)
    })
    return CSRegistration { registration?.cancel() }
}


fun <T : CSHasTouchEvent> T.onTouchMove(
    move: (event: MotionEvent) -> Unit,
) = apply {
    val onMove by weak(move) // To fix leakcanary (false?) report
    onTouchEvent = { event ->
        when (event.actionMasked) {
            ACTION_DOWN -> true
            ACTION_UP, ACTION_CANCEL -> true
            ACTION_MOVE -> true.also { onMove?.invoke(event) }
            else -> false
        }
    }
}

fun <T : CSHasTouchEvent> T.onTouch(
    down: (event: MotionEvent) -> Unit,
    move: (event: MotionEvent) -> Unit,
) = apply {
    val onDown by weak(down) // To fix leakcanary (false?) report
    val onMove by weak(move) // To fix leakcanary (false?) report
    onTouchEvent = { event ->
        when (event.actionMasked) {
            ACTION_DOWN -> true.also { onDown?.invoke(event) }
            ACTION_UP, ACTION_CANCEL -> true
            ACTION_MOVE -> true.also { onMove?.invoke(event) }
            else -> false
        }
    }
}


fun <T : CSHasTouchEvent> T.onTouch(
    down: (event: MotionEvent) -> Unit,
    move: (event: MotionEvent) -> Unit,
    up: (event: MotionEvent) -> Unit,
) = apply {
    val onDown by weak(down)
    val onMove by weak(move)
    val onUp by weak(up)
    onTouchEvent = { event ->
        when (event.actionMasked) {
            ACTION_DOWN -> true.also { onDown?.invoke(event) }
            ACTION_UP, ACTION_CANCEL -> true.also { onUp?.invoke(event) }
            ACTION_MOVE -> true.also { onMove?.invoke(event) }
            else -> false
        }
    }
}


@JvmName("onTouchDownOrMove") fun <T : CSHasTouchEvent> T.onTouch(
    downOrMove: (event: MotionEvent) -> Unit,
) = apply {
    val onDownOrMove by weak(downOrMove)
    onTouchEvent = { event ->
        when (event.actionMasked) {
            ACTION_DOWN -> true.also { onDownOrMove?.invoke(event) }
            ACTION_UP, ACTION_CANCEL -> true
            ACTION_MOVE -> true.also { onDownOrMove?.invoke(event) }
            else -> false
        }
    }
}

inline fun <T : CSHasTouchEvent> T.onTouchDown(
    crossinline down: () -> Unit,
) = onTouch(down = { isDown -> if (isDown) down() })

inline fun <T : CSHasTouchEvent> T.onTouchUp(
    crossinline up: () -> Unit,
) = onTouch(down = { isDown -> if (!isDown) up() })

fun <T : CSHasTouchEvent> T.onTouchUp(
    up: () -> Unit, cancel: () -> Unit,
) = apply {
    val onUp by weak(up)
    val onCancel by weak(cancel)
    onTouchEvent = {
        when (it.actionMasked) {
            ACTION_UP -> false.also { onUp?.invoke() }
            ACTION_CANCEL -> false.also { onCancel?.invoke() }
            else -> false
        }
    }
}

inline fun <T : CSHasTouchEvent> T.onLongTouch(
    crossinline onTouch: (isDown: Boolean) -> Unit,
    crossinline onClick: () -> Unit,
) = apply {
    var isLongTouch = false
    self.onLongClick {
        isLongTouch = true
        onTouch(true)
    }
    onTouchUp(up = {
        if (isLongTouch) {
            isLongTouch = false
            onTouch(false)
        } else onClick()
    }, cancel = {
        if (isLongTouch) {
            isLongTouch = false
            onTouch(false)
        }
    })
}

inline fun <T : CSHasTouchEvent> T.onTouch(
    crossinline down: () -> Unit,
    crossinline up: () -> Unit,
) = onTouch(down = { isDown -> if (isDown) down() else up() })

fun <T : CSHasTouchEvent> T.toggleActiveIf(
    property: CSProperty<Boolean>): CSRegistration {
    setToggleActive(property.value)
    val propertyOnChange = property.onChange { setToggleActive(property.value) }
    onTouchActiveToggle { on -> propertyOnChange.paused { property.value(on) } }
    return propertyOnChange
}

fun <T : CSHasTouchEvent> T.toggleSelectedIf(
    property: CSProperty<Boolean>): CSRegistration {
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
    crossinline function: (Boolean) -> Unit,
) = onTouch(down = { isDown ->
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
) = onTouch(down = { isDown ->
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
    delay: Duration = 1.seconds,
    period: Duration = 250.milliseconds,
    repeat: () -> Unit,
) = onTouch(parent, delay.inWholeMilliseconds.toInt(), period.inWholeMilliseconds.toInt(),
    repeat)

fun CSHasTouchEvent.onTouch(
    parent: CSHasRegistrations,
    delay: Int, period: Int,
    repeat: () -> Unit,
) = onTouch(parent, repeat = { repeat() }, step = { }, delay = delay, period = period,
    until = { true }, onDone = {})

fun <T> CSHasTouchEvent.onTouch(
    parent: CSHasRegistrations,
    repeat: (step: T) -> Unit,
    step: (repeatCount: Int) -> T,
    delay: Int, period: Int,
    until: (step: T) -> Boolean,
    onDone: () -> Unit,
) {
    var repeatCount: Int
    var repeatRegistration: CSRegistration? = null
    onTouch(down = {
        repeatCount = 0
        repeat(step(repeatCount))
        repeatRegistration?.cancel()
        if (self.isEnabled) repeatRegistration =
            parent.laterEach(delay, period, function = {
                repeat(step(repeatCount))
                repeatCount++
                if (!until(step(repeatCount))) {
                    repeatRegistration?.cancel()
                    onDone()
                }
            })
    }, up = {
        repeatRegistration?.cancel()
    })
}
