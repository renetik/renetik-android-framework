package renetik.android.controller.base

import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import renetik.android.framework.Func
import renetik.android.framework.event.CSRegistration
import renetik.android.framework.event.CSRegistration.Companion.registration
import renetik.android.framework.event.listen
import renetik.java.util.CSTimer
import renetik.java.util.CSTimer.scheduleAtFixedRateRunOnUI
import java.util.concurrent.ScheduledFuture

fun CSActivityView<*>.enterFullScreen() {
    WindowCompat.setDecorFitsSystemWindows(activity().window, false)
    val controller = WindowInsetsControllerCompat(activity().window, view)
    controller.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    controller.hide(WindowInsetsCompat.Type.systemBars())
}

fun CSActivityView<*>.exitFullScreen() {
    WindowCompat.setDecorFitsSystemWindows(activity().window, true)
    WindowInsetsControllerCompat(activity().window, view).show(WindowInsetsCompat.Type.systemBars())
}

fun CSActivityView<*>.onResume(listener: Func) = eventResume.listen { listener() }
fun CSActivityView<*>.onPause(listener: Func) = eventPause.listen { listener() }

fun CSActivityView<*>.schedule(interval: Int, function: Func): CSRegistration {
    lateinit var reg: CSRegistration
    fun createScheduler() = scheduleAtFixedRateRunOnUI(
        delay = interval.toLong(), period = interval.toLong()) {
        if (reg.isActive) function()
    }

    var task: ScheduledFuture<*>? = createScheduler()
    val onResumeRegistration = onResume { if (task == null) task = createScheduler() }
    val onPauseRegistration = onPause {
        task?.cancel(true)
        task = null
    }
    reg = registration {
        onResumeRegistration.cancel()
        onPauseRegistration.cancel()
    }
    return reg
}