package renetik.android.ui.extensions.view

import android.view.View
import android.view.ViewPropertyAnimator
import renetik.android.core.lang.CSEnvironment.app
import renetik.android.core.lang.CSLeakCanary

val shortAnimationDuration =
    app.resources.getInteger(android.R.integer.config_shortAnimTime)

val mediumAnimationDuration =
    app.resources.getInteger(android.R.integer.config_mediumAnimTime)

val longAnimationDuration =
    app.resources.getInteger(android.R.integer.config_longAnimTime)

var fadeAnimationDuration = shortAnimationDuration

fun <T : View> T.fadeIn(duration: Int = fadeAnimationDuration): ViewPropertyAnimator? {
    if (isVisible) return null
    if (CSLeakCanary.isEnabled) {
        show()
        return null
    }
    val originalAlpha = alpha; visible(); alpha = 0f
    return animate().alpha(originalAlpha).setDuration(duration.toLong())
        .onAnimationEnd { updateVisibility() }
}

fun <T : View> T.fadeOut(
    duration: Int = fadeAnimationDuration,
    invisible: Boolean = false,
    onDone: (() -> Unit)? = null,
): ViewPropertyAnimator? = when {
    CSLeakCanary.isEnabled -> {
        if (invisible) invisible() else gone()
        onDone?.invoke(); null
    }
    isGone || isInvisible -> {
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
            alpha = originalAlpha
            if (invisible) invisible() else gone()
            onDone?.invoke()
        }
    }
}

