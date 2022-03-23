package renetik.android.view

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import renetik.android.content.dpToPixel
import renetik.android.content.toDp
import renetik.android.framework.event.CSRegistration

fun <T : View> T.hasSize(onHasSize: (View) -> Unit): CSRegistration? {
    if (width == 0 || height == 0) return onLayout {
        if (width != 0 && height != 0) {
            onHasSize(this@hasSize)
            return@onLayout true
        }
        return@onLayout false
    }
    else {
        onHasSize(this)
        return null
    }
}

fun <T : View> T.afterGlobalLayout(action: (View) -> Unit) = object : CSRegistration {
    val listener = OnGlobalLayoutListener {
        if (isActive) {
            cancel()
            action(this@afterGlobalLayout)
        }
    }
    override var isActive = true
    override fun cancel() {
        isActive = false
        viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }

    init {
        viewTreeObserver.addOnGlobalLayoutListener(listener)
    }
}

fun <T : View> T.onGlobalFocus(function: (View?, View?) -> Unit) = object : CSRegistration {
    val listener = OnGlobalFocusChangeListener { old, new -> if (isActive) function(old, new) }
    override var isActive = true
    override fun cancel() {
        isActive = false
        viewTreeObserver.removeOnGlobalFocusChangeListener(listener)
    }

    init {
        viewTreeObserver.addOnGlobalFocusChangeListener(listener)
    }
}

/**
 * @return true to remove listener
 **/
fun <T : View> T.onLayout(action: (View) -> Boolean) = object : CSRegistration {
    val listener = OnGlobalLayoutListener {
        if (isActive && action(this@onLayout)) cancel()
    }
    override var isActive = true
    override fun cancel() {
        isActive = false
        viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }

    init {
        viewTreeObserver.addOnGlobalLayoutListener(listener)
    }
}

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

fun <T : View> T.heightDp(value: Int) = height(context.dpToPixel(value))
fun <T : View> T.widthDp(value: Int) = widthDp(value.toFloat())

fun <T : View> T.widthDp(value: Float) = apply {
    val params = layoutParams
    params.width = context.dpToPixel(value)
    layoutParams = params
}

fun <T : View> T.height(value: Int) = apply {
    val params = layoutParams
    params.height = value
    layoutParams = params
}

fun <T : View> T.height(value: Float) = height(value.toInt())

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
    get() = context.toDp(width)
    set(value) {
        width(context.dpToPixel(value))
    }

var View.heightDp: Int
    get() = context.toDp(height)
    set(value) {
        height(context.dpToPixel(value))
    }

