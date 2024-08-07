package renetik.android.controller.navigation

import android.view.Gravity.CENTER
import android.view.View
import android.widget.FrameLayout.LayoutParams
import androidx.core.view.updateLayoutParams
import renetik.android.controller.base.CSView
import renetik.android.controller.navigation.CSNavigationItemAnimation.Fade
import renetik.android.controller.navigation.CSNavigationItemAnimation.None
import renetik.android.core.lang.CSLeakCanary
import renetik.android.ui.extensions.view.matchParent

fun <T : CSNavigationView> T.selected(button: CSView<*>) = selected(button.view)

fun <T : CSNavigationView> T.selected(button: View) = apply {
    button.isSelected = true
    onClose { button.isSelected = false }
}

fun <T : CSNavigationView> T.show(
    animation: CSNavigationItemAnimation = Fade
) = apply {
    this.animation = if (CSLeakCanary.isEnabled) None else animation
    navigation!!.push(this)
    updateVisibility()
}

fun <T : CSNavigationView> T.center() = apply {
    isFullScreen = false
    animation = Fade
    viewContent.updateLayoutParams<LayoutParams> { gravity = CENTER }
}

val <T : CSNavigationView> T.isClicksBlocked get() = viewContent.isClickable

fun <T : CSNavigationView> T.fullScreen() = apply {
    isFullScreen = true
    viewContent.matchParent()
}
