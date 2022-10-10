package renetik.android.ui.extensions.view

import android.graphics.Rect
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import renetik.android.core.extensions.content.dpToPixel
import renetik.android.core.extensions.content.toDp
import renetik.android.core.lang.Func
import renetik.android.core.lang.variable.CSWeakVariable.Companion.weak
import renetik.android.core.lang.void
import renetik.android.core.math.CSPoint
import renetik.android.core.math.left
import renetik.android.core.math.top
import renetik.android.event.registration.*
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration

val <T : View> T.hasSize get() = width > 0 && height > 0

fun <T : View> T.hasSize(
    parent: CSHasRegistrations, onHasSize: (View) -> Unit) {
    if (width == 0 || height == 0) parent.register(onGlobalLayout {
        if (hasSize) {
            onHasSize(this@hasSize)
            parent.cancel(it)
        }
    })
    else onHasSize(this)
}

fun View.onSizeChange(function: Func): CSRegistration {
    val listener = OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> function() }
    return CSRegistration(onResume = { addOnLayoutChangeListener(listener) },
        onPause = { removeOnLayoutChangeListener(listener) }).start()
}

fun View.onHasSizeChange(function: Func): CSRegistration =
    onSizeChange { if (hasSize) function() }

fun <T : View> T.afterGlobalLayout(
    parent: CSHasRegistrations, function: (View) -> Unit): CSRegistration {
//    val weakParent by weak(parent)
    return parent.register(onGlobalLayout {
        parent.cancel(it)
        function(this)
    })
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
    ).start()
    return registration
}

/**
 * @return true to remove listener
 **/
fun <T : View> T.onGlobalLayout(function: (CSRegistration) -> void): CSRegistration {
    lateinit var registration: CSRegistration
    val listener = OnGlobalLayoutListener {
        if (registration.isActive) function(registration)
    }
    registration = CSRegistration(
        onResume = { viewTreeObserver.addOnGlobalLayoutListener(listener) },
        onPause = { viewTreeObserver.removeOnGlobalLayoutListener(listener) }
    ).start()
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

fun <T : View> T.size(size: Int) = size(size, size)

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

val View.windowRectangle: Rect
    get() = Rect().also { getWindowVisibleDisplayFrame(it) }

val View.rectangle: Rect
    get() = Rect(left, top, right, bottom)


val View.rectangleInWindow: Rect
    get() {
        val location: CSPoint<Int> = locationInWindow
        val rectangle: Rect = rectangle
        return Rect(location.left, location.top,
            location.left + rectangle.width(),
            location.top + rectangle.height())
    }

