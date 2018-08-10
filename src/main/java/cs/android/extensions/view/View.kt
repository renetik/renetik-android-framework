package cs.android.extensions.view

import android.animation.Animator
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import cs.android.view.adapter.AnimatorAdapter

fun <T : View> T.visible(): Boolean = visibility == VISIBLE

fun <T : View> T.shown(): Boolean = isShown

fun <T : View> T.visible(visible: Boolean): T {
    visibility = if (visible) VISIBLE else GONE
    return this
}

fun <T : View> T.hide(): T {
    visibility = GONE
    return this;
}

fun <T : View> T.show(): T {
    visibility = VISIBLE
    return this;
}

fun <T : View> T.fadeIn(): ViewPropertyAnimator? {
    if (visible()) return null
    show()
    alpha = 0f
    return animate().alpha(1.0f).setDuration(150)
            .setInterpolator(AccelerateDecelerateInterpolator()).setListener(null)
}

fun <T : View> T.fadeOut(): ViewPropertyAnimator? {
    return if (!visible() || alpha == 0f) null else animate().alpha(0f).setDuration(300)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : AnimatorAdapter() {
                override fun onAnimationEnd(animator: Animator?) {
                    hide()
                }
            })
}

fun <T : View> T.onClick(onClick: (view: T) -> Unit): View {
    setOnClickListener { onClick(this) }
    return this
}