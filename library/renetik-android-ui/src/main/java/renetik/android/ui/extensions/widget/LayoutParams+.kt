package renetik.android.ui.extensions.widget

import android.view.Gravity.CENTER
import android.view.Gravity.NO_GRAVITY
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import android.widget.LinearLayout

val layoutMatch get() = MarginLayoutParams(MATCH_PARENT, MATCH_PARENT)
val layoutMatchWrap get() = MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT)
val layoutWrap get() = MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT)
val layoutWrapMatch get() = MarginLayoutParams(WRAP_CONTENT, MATCH_PARENT)
fun layoutWidthHeight(width: Int, height: Int) = MarginLayoutParams(width, height)
fun layoutWidthMatch(width: Int) = MarginLayoutParams(width, MATCH_PARENT)
fun layoutMatchHeight(height: Int) = MarginLayoutParams(MATCH_PARENT, height)
fun MarginLayoutParams.toMatchWrap() = apply { width = MATCH_PARENT; height = WRAP_CONTENT }
fun MarginLayoutParams.toWrapMatch() = apply { width = WRAP_CONTENT; height = MATCH_PARENT }

val layoutMatchCenter get() = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, CENTER)
val layoutMatchWrapCenter get() = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, CENTER)
val layoutWrapMatchCenter get() = FrameLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT, CENTER)
val layoutWrapCenter get() = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, CENTER)
fun layoutWrap(gravity: Int) = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, gravity)
fun layoutWrapHeight(height: Int, gravity: Int? = null) =
    FrameLayout.LayoutParams(WRAP_CONTENT, height, gravity ?: NO_GRAVITY)

fun layoutMatchHeight(height: Int, gravity: Int? = null) =
    FrameLayout.LayoutParams(MATCH_PARENT, height, gravity ?: NO_GRAVITY)

val layoutMatchFill get() = LinearLayout.LayoutParams(MATCH_PARENT, 0, 1f)
val layoutWrapFill get() = LinearLayout.LayoutParams(WRAP_CONTENT, 0, 1f)
val layoutFillMatch get() = LinearLayout.LayoutParams(0, MATCH_PARENT, 1f)
val layoutFillWrap get() = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
fun layoutWidthFill(width: Int) = LinearLayout.LayoutParams(width, 0, 1f)
fun layoutFillHeight(height: Int) = LinearLayout.LayoutParams(0, height, 1f)
fun LinearLayout.LayoutParams.toFillMatch() = apply {
    width = 0; height = MATCH_PARENT; weight = 1f
}

fun LinearLayout.LayoutParams.toFillWrap() = apply {
    width = 0; height = WRAP_CONTENT; weight = 1f
}
