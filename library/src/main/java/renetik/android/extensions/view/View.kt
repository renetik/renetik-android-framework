package renetik.android.extensions.view

import android.animation.Animator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import renetik.android.view.adapter.CSAnimatorAdapter

val <T : View> T.isVisible get() = visibility == VISIBLE

fun <T : View> T.visible(visible: Boolean) = apply { visibility = if (visible) VISIBLE else GONE }

fun <T : View> T.hide() = apply { visibility = GONE }

fun <T : View> T.show() = apply { visibility = VISIBLE }

val <T : View> T.parentView get() = parent as? View

fun <T : View> T.removeFromSuperview() = apply { (parent as? ViewGroup)?.removeView(this) }

fun <T : View> View.findViewRecursive(id: Int): T? = findView<T>(id)
        ?: parentView?.findViewRecursive<T>(id)

fun <T : View> View.findView(id: Int): T? = findViewById<T>(id)

fun <T : View> T.fade(fadeIn: Boolean) = if (fadeIn) fadeIn() else fadeOut()

fun <T : View> T.fadeIn(): ViewPropertyAnimator? {
    if (isVisible) return null
    show()
    alpha = 0f
    return animate().alpha(1.0f).setDuration(150)
            .setInterpolator(AccelerateDecelerateInterpolator()).setListener(null)
}

fun <T : View> T.fadeOut(): ViewPropertyAnimator? {
    return if (!isVisible || alpha == 0f) null else animate().alpha(0f).setDuration(300)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : CSAnimatorAdapter() {
                override fun onAnimationEnd(animator: Animator?) {
                    hide()
                }
            })
}

fun <T : View> T.onClick(onClick: (view: T) -> Unit) = apply { setOnClickListener { onClick(this) } }

fun <T : View> T.hasSize(onHasSize: (View) -> Unit) {
    val self = this
    if (width == 0 && height == 0)
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                onHasSize(self)
            }
        })
    else onHasSize(this)
}

fun <T : View> T.width(function: (Int) -> Unit) = hasSize { function(width) }

fun <T : View> T.height(function: (Int) -> Unit) = hasSize { function(height) }

fun <T : View> T.setMargins(left: Int, top: Int, right: Int, bottom: Int) {
    val params = layoutParams as? ViewGroup.MarginLayoutParams
    params?.setMargins(left, top, right, bottom)
    layoutParams = params
}

fun <T : View> T.bottomMargin(bottom: Int) {
    val params = layoutParams as? ViewGroup.MarginLayoutParams
    params?.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottom)
    layoutParams = params
}

fun <T : View> T.topMargin(top: Int) {
    val params = layoutParams as? ViewGroup.MarginLayoutParams
    params?.setMargins(params.leftMargin, top, params.rightMargin, params.bottomMargin)
    layoutParams = params
}

fun <T : View> T.createBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    Canvas(bitmap).apply {
        background?.draw(this) ?: this.drawColor(Color.WHITE)
        draw(this)
    }
    return bitmap
}


