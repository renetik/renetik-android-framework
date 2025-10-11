package renetik.android.ui.extensions.view

import android.view.View
import android.view.ViewPropertyAnimator
import renetik.android.core.lang.CSLeakCanary
import renetik.android.event.common.CSHasDestruct

const val shortAnimationDuration = 200
const val mediumAnimationDuration = 400
const val longAnimationDuration = 500
const val fadeAnimationDuration = shortAnimationDuration

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
    parent: CSHasDestruct,
    duration: Int = fadeAnimationDuration,
    invisible: Boolean = false,
    onDone: (() -> Unit)
) = fadeOut(duration, invisible, onDone = {
    if (!parent.isDestructed) onDone()
})

fun <T : View> T.fadeOut(
    duration: Int = fadeAnimationDuration,
    invisible: Boolean = false,
    onDone: (() -> Unit) = {}
): ViewPropertyAnimator? = when {
    CSLeakCanary.isEnabled -> {
        if (invisible) invisible() else gone()
        onDone(); null
    }
    isGone || isInvisible -> {
        onDone(); null
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
            if (isAttachedToWindow) onDone()
        }
    }
}

