package renetik.android.ui.view

import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.action
import renetik.android.event.registration.laterEach
import renetik.android.event.registration.launch
import renetik.android.event.registration.paused
import renetik.android.event.registration.plus
import renetik.android.ui.extensions.view.onLongClick
import renetik.android.ui.extensions.view.pressed
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

inline fun <T : CSHasTouchEvent> T.onTouch(
    parent: CSHasRegistrations,
    crossinline onTouch: (isDown: Boolean) -> Unit,
) = apply { parent + onTouch(onTouch = onTouch) }

inline fun <T : CSHasTouchEvent> T.onTouch(
    crossinline onTouch: (isDown: Boolean) -> Unit,
): CSRegistration = eventOnTouch.listen {
    when (it.actionMasked) {
        ACTION_DOWN -> it.handled().also {
            self.pressed(true)
            onTouch(true)
        }

        ACTION_UP, ACTION_CANCEL -> it.handled().also {
            self.pressed(false)
            onTouch(false)
        }

        ACTION_MOVE -> it.handled()
    }
}

inline fun <T : CSHasTouchEvent> T.onLongTouch(
    duration: Duration = 2.seconds,
    crossinline down: (isDown: Boolean) -> Unit,
): CSRegistration {
    var registration: CSRegistration? = null
    val eventRegistration = onTouch(onTouch = { isDown ->
        if (isDown) registration = Main.launch {
            delay(duration)
            if (it.isActive) {
                down(true)
                registration = null
            }
        }
        else registration?.cancel() ?: down(false)
    })
    return CSRegistration {
        eventRegistration.cancel()
        registration?.cancel()
    }
}


inline fun <T : CSHasTouchEvent> T.onTouchMove(
    crossinline move: (event: MotionEvent) -> Unit,
): CSRegistration = eventOnTouch.listen {
    when (it.actionMasked) {
        ACTION_DOWN -> it.handled()
        ACTION_UP, ACTION_CANCEL -> it.handled()
        ACTION_MOVE -> it.handled().run { move(it.event) }
    }
}

inline fun <T : CSHasTouchEvent> T.onTouch(
    crossinline down: (event: MotionEvent) -> Unit,
    crossinline move: (event: MotionEvent) -> Unit,
): CSRegistration = eventOnTouch.listen {
    when (it.actionMasked) {
        ACTION_DOWN -> it.handled().run { down(it.event) }
        ACTION_UP, ACTION_CANCEL -> it.handled()
        ACTION_MOVE -> it.handled().run { move(it.event) }
    }
}

inline fun <T : CSHasTouchEvent> T.onTouch(
    crossinline down: (event: MotionEvent) -> Unit = {},
    crossinline move: (event: MotionEvent) -> Unit,
    crossinline up: (event: MotionEvent) -> Unit,
): CSRegistration = eventOnTouch.listen {
    when (it.actionMasked) {
        ACTION_DOWN -> it.handled().run { down(it.event) }
        ACTION_UP, ACTION_CANCEL -> it.handled().run { up(it.event) }
        ACTION_MOVE -> it.handled().run { move(it.event) }
    }
}

@JvmName("onTouchDownOrMove")
inline fun <T : CSHasTouchEvent> T.onTouch(
    crossinline downOrMove: (event: MotionEvent) -> Unit,
): CSRegistration = eventOnTouch.listen {
    when (it.actionMasked) {
        ACTION_DOWN -> it.handled().run { downOrMove(it.event) }
        ACTION_UP, ACTION_CANCEL -> it.handled()
        ACTION_MOVE -> it.handled().run { downOrMove(it.event) }
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

inline fun <T : CSHasTouchEvent> T.onTouchUp(
    crossinline up: () -> Unit, crossinline cancel: () -> Unit,
): CSRegistration = eventOnTouch.listen {
    when (it.actionMasked) {
        ACTION_UP -> up()
        ACTION_CANCEL -> cancel()
    }
}

inline fun <T : CSHasTouchEvent> T.onLongTouch(
    crossinline onTouch: (isDown: Boolean) -> Unit,
    crossinline onClick: () -> Unit,
): CSRegistration {
    var isLongTouch = false
    self.onLongClick {
        isLongTouch = true
        onTouch(true)
    }
    return onTouchUp(up = {
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
): CSRegistration = onTouch(onTouch = { isDown -> if (isDown) down() else up() })

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
    delay: Duration = 1.seconds,
    period: Duration = 250.milliseconds,
    repeat: () -> Unit,
): CSRegistration = onTouch(parent,
    delay.inWholeMilliseconds.toInt(),
    period.inWholeMilliseconds.toInt(),
    repeat)

fun CSHasTouchEvent.onTouch(
    parent: CSHasRegistrations,
    delay: Int, period: Int,
    repeat: () -> Unit,
): CSRegistration = onTouch(parent,
    repeat = { repeat() },
    step = { },
    delay = delay,
    period = period,
    until = { true },
    onDone = {})

fun <T> CSHasTouchEvent.onTouch(
    parent: CSHasRegistrations,
    repeat: (step: T) -> Unit,
    step: (repeatCount: Int) -> T,
    delay: Int, period: Int,
    until: (step: T) -> Boolean,
    onDone: () -> Unit,
): CSRegistration {
    var repeatCount: Int
    var repeatRegistration: CSRegistration? = null
    val registration = onTouch(down = {
        repeatCount = 0
        step(repeatCount)?.also {
            if (!until(it)) onDone() else repeat(it)
        }
        repeatRegistration?.cancel()
        if (self.isEnabled) repeatRegistration = parent.laterEach(delay, period, function = {
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
    return CSRegistration(repeatRegistration, registration)
}
