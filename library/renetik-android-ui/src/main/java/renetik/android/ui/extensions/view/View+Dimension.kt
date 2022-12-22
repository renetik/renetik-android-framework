package renetik.android.ui.extensions.view

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.updateLayoutParams
import renetik.android.core.extensions.content.dpToPixel
import renetik.android.core.extensions.content.toDp
import renetik.android.core.math.CSPoint
import renetik.android.core.math.left
import renetik.android.core.math.top

val <T : View> T.hasSize get() = width > 0 && height > 0

fun <T : View> T.margins(left: Int, top: Int, right: Int, bottom: Int) = apply {
    updateLayoutParams<MarginLayoutParams> { setMargins(left, top, right, bottom) }
}

fun <T : View> T.margin(value: Int) = apply {
    updateLayoutParams<MarginLayoutParams> { setMargins(value, value, value, value) }
}

fun <T : View> T.marginDp(value: Int) = margin(context.dpToPixel(value))

fun <T : View> T.bottomMargin(value: Int) = apply {
    updateLayoutParams<MarginLayoutParams> { setMargins(leftMargin, topMargin, rightMargin, value) }
}

fun <T : View> T.topMargin(value: Int) = apply {
    updateLayoutParams<MarginLayoutParams> {
        setMargins(leftMargin, value, rightMargin, bottomMargin)
    }
}

fun <T : View> T.startMargin(value: Int) = apply {
    updateLayoutParams<MarginLayoutParams> {
        setMargins(value, topMargin, rightMargin, bottomMargin)
    }
}

fun <T : View> T.endMargin(value: Int) = apply {
    updateLayoutParams<MarginLayoutParams> { setMargins(leftMargin, topMargin, value, bottomMargin) }
}

fun <T : View> T.horizontalMarginDp(value: Int) = horizontalMargin(context.dpToPixel(value))

fun <T : View> T.horizontalMargin(value: Int) = apply {
    updateLayoutParams<MarginLayoutParams> { setMargins(value, topMargin, value, bottomMargin) }
}

fun <T : View> T.verticalMargin(value: Int) = apply {
    updateLayoutParams<MarginLayoutParams> { setMargins(leftMargin, value, rightMargin, value) }
}

fun <T : View> T.size(width: Int, height: Int) = apply {
    width(width)
    height(height)
}

fun <T : View> T.size(size: Int) = size(size, size)

fun <T : View> T.width(value: Int) = apply {
    updateLayoutParams<LayoutParams> { width = value }
}

fun <T : View> T.heightDp(value: Int) = height(context.dpToPixel(value))
fun <T : View> T.widthDp(value: Int) = widthDp(value.toFloat())

fun <T : View> T.widthDp(value: Float) = apply {
    updateLayoutParams<LayoutParams> { width = context.dpToPixel(value) }
}

fun <T : View> T.height(value: Int) = apply {
    updateLayoutParams<LayoutParams> { height = value }
}

fun <T : View> T.widthWrap() = apply {
    updateLayoutParams<LayoutParams> { width = WRAP_CONTENT }
}

fun <T : View> T.heightWrap() = apply {
    updateLayoutParams<LayoutParams> { height = WRAP_CONTENT }
}

fun <T : View> T.matchParent() = apply {
    updateLayoutParams<LayoutParams> {
        width = MATCH_PARENT
        height = MATCH_PARENT
    }
}

fun <T : View> T.wrapContent() = apply {
    updateLayoutParams<LayoutParams> {
        width = WRAP_CONTENT
        height = WRAP_CONTENT
    }
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