package renetik.android.view.extensions

import android.animation.Animator
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewPropertyAnimator
import renetik.android.base.CSApplicationInstance.application
import renetik.android.view.adapter.CSAnimatorAdapter

val shortAnimationDuration =
    application.resources.getInteger(android.R.integer.config_shortAnimTime)

fun <T : View> T.fadeIn(duration: Int = shortAnimationDuration): ViewPropertyAnimator? {
    if (isVisible) return null
    visibility = VISIBLE
    alpha = 0f
    return animate().alpha(1f).setDuration(duration.toLong())
//        .setInterpolator(AccelerateDecelerateInterpolator())
        .setListener(null)
}

fun <T : View> T.fadeOut(
    duration: Int = shortAnimationDuration,
    onDone: (() -> Unit)? = null
): ViewPropertyAnimator? {
    if (isGone) {
        onDone?.invoke()
        return null
    } else if (alpha == 0f) {
        hide()
        return null
    } else {
        isClickable = false
        return animate().alpha(0f).setDuration(duration.toLong())
//            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : CSAnimatorAdapter() {
                override fun onAnimationEnd(animator: Animator?) {
                    isClickable = true
                    visibility = GONE
                    onDone?.invoke()
                }
            })
    }
}

fun <T : View> T.fade(fadeIn: Boolean) = if (fadeIn) fadeIn() else fadeOut()

var <T : View> T.isFadeVisible
    get() = visibility == VISIBLE
    set(value) {
        fade(fadeIn = value)
    }

