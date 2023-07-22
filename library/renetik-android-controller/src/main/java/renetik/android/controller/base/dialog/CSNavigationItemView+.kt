package renetik.android.controller.base.dialog

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import renetik.android.controller.base.dialog.DialogAnimation.Fade
import renetik.android.controller.base.dialog.DialogAnimation.None
import renetik.android.core.lang.CSLeakCanary
import renetik.android.core.lang.variable.setFalse

fun <T : CSNavigationItemView<*>> T.selectedButton(button: View) = apply {
    button.isSelected = true
    onClose { button.isSelected = false }
}

fun <T : CSNavigationItemView<*>> T.show(animation: DialogAnimation = Fade) = apply {
    this.animation = if (CSLeakCanary.isEnabled) None else animation
    navigation!!.push(this)
    updateVisibility()
}

fun <T : CSNavigationItemView<*>> T.center(width: Int? = null, height: Int? = null) = apply {
    isFullscreenNavigationItem.setFalse()
    animation = Fade
    dialogContent.updateLayoutParams<FrameLayout.LayoutParams> {
        gravity = Gravity.CENTER
        width?.let { this.width = it }
        height?.let { this.height = it }
    }
}

fun <T : CSNavigationItemView<*>> T.passClicksUnder(pass: Boolean = true) = apply {
    dialogContent.apply {
        isClickable = !pass
        isFocusable = !pass
    }
}

val <T : CSNavigationItemView<*>> T.isClicksBlocked get() = dialogContent.isClickable
