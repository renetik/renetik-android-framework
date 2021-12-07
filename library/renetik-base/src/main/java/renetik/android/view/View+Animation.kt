package renetik.android.view

import android.view.View
import android.view.ViewPropertyAnimator
import renetik.android.framework.CSApplication.Companion.application

val shortAnimationDuration =
    application.resources.getInteger(android.R.integer.config_shortAnimTime)

val mediumAnimationDuration =
    application.resources.getInteger(android.R.integer.config_mediumAnimTime)

fun <T : View> T.fadeIn(duration: Int = mediumAnimationDuration): ViewPropertyAnimator? {
    if (isVisible) return null
    val originalAlpha = alpha; visible(); alpha = 0f
    return animate().alpha(originalAlpha).setDuration(duration.toLong())
}

fun <T : View> T.fadeOut(
    duration: Int = shortAnimationDuration,
    invisible: Boolean = false,
    onDone: (() -> Unit)? = null): ViewPropertyAnimator? = when {
    isGone -> {
        onDone?.invoke(); null
    }
    alpha == 0f -> {
        if (invisible) invisible() else gone()
        null
    }
    else -> {
        isClickable = false
        val originalAlpha = alpha
        animate().alpha(0f).setDuration(duration.toLong()).onAnimationEnd {
            isClickable = true
            if (invisible) invisible() else gone()
            alpha = originalAlpha
            onDone?.invoke()
        }
    }
}

