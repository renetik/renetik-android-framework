@file:Suppress("NOTHING_TO_INLINE")

package renetik.android.core.android.graphics

import android.graphics.Point
import android.graphics.Rect
import renetik.android.core.kotlin.primitives.isTrue
import renetik.android.core.math.CSPoint
import kotlin.math.max
import kotlin.math.min

inline val Rect.width get() = width()

inline val Rect.height get() = height()

inline val Rect.debugString get() = "left:$left top:$top width:$width height:$height"

inline fun Rect.load(start: CSPoint<Float>, end: CSPoint<Float>) = apply {
    val left = min(start.x, end.x)
    val top = min(start.y, end.y)
    val right = max(start.x, end.x)
    val bottom = max(start.y, end.y)
    set(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
}

inline fun Rect.clear() = set(0, 0, 0, 0)

inline fun Rect.offsetToNewLeft(value: Int) = offsetTo(value, top)

inline fun Rect.offsetToNewRight(value: Int) = offsetTo(value - width, top)

inline fun Rect.offsetToNewTop(value: Int) = offsetTo(left, value)

inline fun Rect.offsetToNewBottom(value: Int) = offsetTo(left, value - height)

inline fun Rect.copy(): Rect = Rect(this)

inline operator fun Rect?.contains(p: Point): Boolean = this?.contains(p.x, p.y).isTrue