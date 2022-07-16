package renetik.android.ui.extensions.view

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import renetik.android.core.extensions.content.dpToPixel
import renetik.android.core.extensions.content.toDp
import renetik.android.core.lang.void
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration

fun <T : View> T.hasSize(onHasSize: (View) -> Unit): CSRegistration? =
    if (width == 0 || height == 0) onGlobalLayout {
        if (width != 0 && height != 0) {
            onHasSize(this@hasSize)
            it.cancel()
        }
    }
    else {
        onHasSize(this)
        null
    }

fun <T : View> T.afterGlobalLayout(
    function: (View) -> Unit): CSRegistration = onGlobalLayout {
    function(this)
    it.cancel()
}

fun <T : View> T.onGlobalFocus(
    function: (View?, View?) -> Unit): CSRegistration {
    lateinit var registration: CSRegistration
    val listener = OnGlobalFocusChangeListener { old, new ->
        if (registration.isActive) function(old, new)
    }
    registration = CSRegistration(
        onResume = { viewTreeObserver.addOnGlobalFocusChangeListener(listener) },
        onPause = { viewTreeObserver.removeOnGlobalFocusChangeListener(listener) }
    )
    return registration
}

/**
 * @return true to remove listener
 **/
fun <T : View> T.onGlobalLayout(action: (CSRegistration) -> void): CSRegistration {
    lateinit var registration: CSRegistration
    val listener = OnGlobalLayoutListener {
        if (registration.isActive) action(registration)
    }
    registration = CSRegistration(
        onResume = { viewTreeObserver.addOnGlobalLayoutListener(listener) },
        onPause = { viewTreeObserver.removeOnGlobalLayoutListener(listener) }
    )
    return registration
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

