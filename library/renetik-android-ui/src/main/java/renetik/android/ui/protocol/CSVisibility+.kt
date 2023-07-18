package renetik.android.ui.protocol

import renetik.android.core.lang.CSHandler.postOnMain
import renetik.android.core.lang.Func
import renetik.android.event.listen
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.task.CSBackground.backgroundRepeat
import java.util.concurrent.ScheduledFuture

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
    fun createScheduler() = backgroundRepeat(interval = period, delay = delay) {
        if (registration.isActive) postOnMain { if (!registration.isCanceled) function() }
    }

//    fun createScheduler() = scheduleAtFixedRateRunOnUI(
//        delay = delay.toLong(), period = period.toLong()
//    ) {
//        if (registration.isActive) function()
//    }

    var task: ScheduledFuture<*>? = createScheduler()
    val onResumeRegistration = onShowing { -> if (task == null) task = createScheduler() }
    val onPauseRegistration = onHiding { ->
        task?.cancel(true)
        task = null
    }
    registration = CSRegistration(isActive = true, onCancel = {
        onResumeRegistration.cancel()
        onPauseRegistration.cancel()
    })
    return registration
}