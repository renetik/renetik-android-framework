package renetik.android.view.extensions

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

fun <T : View> T.hasSize(onHasSize: (View) -> Unit) {
    val self = this
    if (width == 0 || height == 0)
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (width != 0 && height != 0) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    onHasSize(self)
                }
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




