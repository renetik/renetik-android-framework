package renetik.android.controller.base

import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import renetik.android.core.lang.Func
import renetik.android.event.listen

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

//val statusBarHeight get() =  activity().activityView!!.view.locationOnScreen.y