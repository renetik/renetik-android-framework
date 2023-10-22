package renetik.android.controller.navigation

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import androidx.core.view.updateLayoutParams
import renetik.android.controller.navigation.CSNavigationItemAnimation.Fade
import renetik.android.controller.navigation.CSNavigationItemAnimation.None
import renetik.android.core.lang.CSLeakCanary
import renetik.android.core.lang.variable.setFalse

fun <T : CSNavigationItemView<*>> T.selectedButton(button: View) = apply {
    button.isSelected = true
    onClose { button.isSelected = false }
}

fun <T : CSNavigationItemView<*>> T.show(animation: CSNavigationItemAnimation = Fade) = apply {
    this.animation = if (CSLeakCanary.isEnabled) None else animation
    navigation!!.push(this)
    updateVisibility() // TODO needed ?
}

fun <T : CSNavigationItemView<*>> T.center() = apply {
    isFullscreenNavigationItem.setFalse()
    animation = Fade
    dialogContent.updateLayoutParams<LayoutParams> { gravity = Gravity.CENTER }
}

fun <T : CSNavigationItemView<*>> T.passClicksUnder(pass: Boolean = true) = apply {
    dialogContent.apply {
        isClickable = !pass
        isFocusable = !pass
    }
}

val <T : CSNavigationItemView<*>> T.isClicksBlocked get() = dialogContent.isClickable
