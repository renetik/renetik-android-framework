package renetik.android.view.extensions

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import renetik.android.framework.CSApplication.Companion.application
import renetik.android.content.toDp
import renetik.android.content.dpToPixel

fun <T : View> T.hasSize(onHasSize: (View) -> Unit) = apply {
    if (width == 0 || height == 0) onLayout {
        if (width != 0 && height != 0) {
            onHasSize(this@hasSize)
            return@onLayout true
        }
        return@onLayout false
    }
    else onHasSize(this)
}

fun <T : View> T.afterLayout(action: (View) -> Unit) = apply {
    viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            action(this@afterLayout)
        }
    })
}

/**
 * @return true to remove listener
 **/
fun <T : View> T.onLayout(action: (View) -> Boolean) = apply {
    viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (action(this@onLayout)) viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    })
}

fun <T : View> T.width(function: (Int) -> Unit) = hasSize { function(width) }

fun <T : View> T.height(function: (Int) -> Unit) = hasSize { function(height) }

fun <T : View> T.margins(left: Int, top: Int, right: Int, bottom: Int) = apply {
    layoutParams = (layoutParams as MarginLayoutParams).apply {
        setMargins(left, top, right, bottom)
    }
}

fun <T : View> T.margin(value: Int) = apply {
    layoutParams = (layoutParams as MarginLayoutParams).apply {
        setMargins(value, value, value, value)
    }
}

fun <T : View> T.marginDp(value: Int) = margin(context.dpToPixel(value))

fun <T : View> T.bottomMargin(value: Int) = apply {
    layoutParams = (layoutParams as MarginLayoutParams).apply {
        setMargins(leftMargin, topMargin, rightMargin, value)
    }
}

fun <T : View> T.topMargin(value: Int) = apply {
    layoutParams = (layoutParams as MarginLayoutParams)
        .apply { setMargins(leftMargin, value, rightMargin, bottomMargin) }
}

fun <T : View> T.startMargin(value: Int) = apply {
    layoutParams = (layoutParams as MarginLayoutParams).apply {
        setMargins(value, topMargin, rightMargin, bottomMargin)
    }
}

fun <T : View> T.endMargin(value: Int) = apply {
    layoutParams = (layoutParams as MarginLayoutParams).apply {
        setMargins(leftMargin, topMargin, value, bottomMargin)
    }
}

fun <T : View> T.horizontalMarginDp(value: Int) = horizontalMargin(context.dpToPixel(value))

fun <T : View> T.horizontalMargin(value: Int) = apply {
    layoutParams = (layoutParams as MarginLayoutParams).apply {
        setMargins(value, topMargin, value, bottomMargin)
    }
}

fun <T : View> T.verticalMargin(value: Int) = apply {
    layoutParams = (layoutParams as MarginLayoutParams).apply {
        setMargins(leftMargin, value, rightMargin, value)
    }
}

fun <T : View> T.size(width: Int, height: Int) = apply {
    width(width)
    height(height)
}

fun <T : View> T.width(value: Int) = apply {
    val params = layoutParams
    params.width = value
    layoutParams = params
}

fun <T : View> T.height(value: Int) = apply {
    val params = layoutParams
    params.height = value
    layoutParams = params
}

var View.layoutWidth: Int
    get() = layoutParams.width
    set(value) {
        width(value)
    }

var View.layoutHeight: Int
    get() = layoutParams.height
    set(value) {
        height(value)
    }

var View.widthDp: Int
    get() = application.toDp(width)
    set(value) {
        width(application.dpToPixel(value))
    }

var View.heightDp: Int
    get() = application.toDp(height)
    set(value) {
        height(application.dpToPixel(value))
    }

