package renetik.android.framework.event

import renetik.android.framework.Func
import renetik.java.util.CSTimer.scheduleAtFixedRateRunOnUI
import java.util.concurrent.ScheduledFuture

fun CSVisibility.whileShowingTrue(function: (Boolean) -> Unit) {
    if (isVisible) function(true)
    eventVisibility.listen { if (it) function(true) else function(false) }
}

fun CSVisibility.onShowing(listener: Func) = eventVisibility.listen { if (it) listener() }

fun CSVisibility.onHiding(listener: Func) = eventVisibility.listen { if (!it) listener() }

fun CSVisibility.task(period: Int, function: Func) = task(period, period, function)

fun CSVisibility.task(delay: Int, period: Int, function: Func): CSRegistration {
    lateinit var reg: CSRegistration
    fun createScheduler() = scheduleAtFixedRateRunOnUI(
        delay = delay.toLong(), period = period.toLong()) {
        if (reg.isActive) function()
    }

    var task: ScheduledFuture<*>? = createScheduler()
    val onResumeRegistration = onShowing { if (task == null) task = createScheduler() }
    val onPauseRegistration = onHiding {
        task?.cancel(true)
        task = null
    }
    reg = CSRegistration.registration {
        onResumeRegistration.cancel()
        onPauseRegistration.cancel()
    }
    return reg
}