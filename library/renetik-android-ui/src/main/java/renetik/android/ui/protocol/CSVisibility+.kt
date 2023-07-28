package renetik.android.ui.protocol

import renetik.android.core.kotlin.primitives.ifTrueReturn
import renetik.android.core.lang.CSHandler.main
import renetik.android.core.lang.Func
import renetik.android.event.listen
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.laterEach

fun CSVisibility.onShowing(function: () -> Unit): CSRegistration = onShowing { _ -> function() }

fun CSVisibility.onShowing(function: (CSRegistration) -> Unit): CSRegistration =
    eventVisibility.listen { registration, visible -> if (visible) function(registration) }

fun CSVisibility.onHiding(function: () -> Unit): CSRegistration = onHiding { _ -> function() }

fun CSVisibility.onHiding(function: (CSRegistration) -> Unit): CSRegistration =
    eventVisibility.listen { registration, visible -> if (!visible) function(registration) }

fun CSVisibility.onVisibility(function: (Boolean) -> Unit): CSRegistration =
    eventVisibility.listen(function)

fun CSVisibility.onVisibility(function: (CSRegistration, Boolean) -> Unit): CSRegistration =
    eventVisibility.listen(function)

fun CSVisibility.whileShowingTrue(function: (Boolean) -> Unit): CSRegistration {
    if (isVisible) function(true)
    return eventVisibility.listen { if (it) function(true) else function(false) }
}

fun CSVisibility.task(period: Int, function: Func) = task(period, period, function)

fun CSVisibility.task(delay: Int, period: Int, function: Func): CSRegistration {
    lateinit var registration: CSRegistration
    fun createScheduler() = main.laterEach(after = delay, period) {
        if (registration.isActive) function()
    }

    var task: CSRegistration? = isVisible.ifTrueReturn(::createScheduler)
    val onVisibilityRegistration = onVisibility { isShowing ->
        if (isShowing) task = createScheduler() else task?.cancel()
    }
    registration = CSRegistration(isActive = true) { onVisibilityRegistration.cancel() }
    return registration
}