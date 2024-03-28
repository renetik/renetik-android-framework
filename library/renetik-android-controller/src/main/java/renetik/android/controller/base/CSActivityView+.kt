package renetik.android.controller.base

import android.view.View
import androidx.annotation.IdRes
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import renetik.android.core.lang.Func

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

val CSActivityView<*>.isPaused get() = !isResumed

fun <T : View> CSActivityView<*>.activityView(
    @IdRes viewId: Int, init: (CSActivityView<T>).() -> Unit
): CSActivityView<T> = object : CSActivityView<T>(this, viewId) {
    init {
        init()
    }
}