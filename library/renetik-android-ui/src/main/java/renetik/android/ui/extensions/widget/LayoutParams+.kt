package renetik.android.ui.extensions.widget

import android.view.Gravity.CENTER
import android.view.Gravity.NO_GRAVITY
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout.LayoutParams
import android.widget.LinearLayout

val layoutMatch get() = LayoutParams(MATCH_PARENT, MATCH_PARENT)
val layoutMatchCenter get() = LayoutParams(MATCH_PARENT, MATCH_PARENT, CENTER)
val layoutMatchWrap get() = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
val layoutMatchWrapCenter get() = LayoutParams(MATCH_PARENT, WRAP_CONTENT, CENTER)
val layoutWrapMatchCenter get() = LayoutParams(WRAP_CONTENT, MATCH_PARENT, CENTER)
val layoutMatchFill get() = LinearLayout.LayoutParams(MATCH_PARENT, 0, 1f)
val layoutWrapFill get() = LinearLayout.LayoutParams(WRAP_CONTENT, 0, 1f)
val layoutWrap get() = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
val layoutWrapCenter get() = LayoutParams(WRAP_CONTENT, WRAP_CONTENT, CENTER)
fun layoutWrap(gravity: Int) = LayoutParams(WRAP_CONTENT, WRAP_CONTENT, gravity)
val layoutWrapMatch get() = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
val layoutFillMatch get() = LinearLayout.LayoutParams(0, MATCH_PARENT, 1f)
val layoutFillWrap get() = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)

fun LinearLayout.LayoutParams.toFillMatch() = apply {
    width = 0; height = MATCH_PARENT; weight = 1f
}

fun LinearLayout.LayoutParams.toFillWrap() = apply {
    width = 0; height = WRAP_CONTENT; weight = 1f
}

fun ViewGroup.LayoutParams.toMatchWrap() = apply {
    width = MATCH_PARENT; height = WRAP_CONTENT
}

fun ViewGroup.LayoutParams.toWrapMatch() = apply {
    width = WRAP_CONTENT; height = MATCH_PARENT
}

fun layoutWrapHeight(height: Int, gravity: Int? = null) =
    LayoutParams(WRAP_CONTENT, height, gravity ?: NO_GRAVITY)

fun layoutMatchHeight(height: Int, gravity: Int? = null) =
    LayoutParams(MATCH_PARENT, height, gravity ?: NO_GRAVITY)

fun layoutWidthHeight(width: Int, height: Int) = LayoutParams(width, height)
fun layoutWidthMatch(width: Int) = LayoutParams(width, MATCH_PARENT)
fun layoutWidthFill(width: Int) = LinearLayout.LayoutParams(width, 0, 1f)
fun layoutMatchHeight(height: Int) = LayoutParams(MATCH_PARENT, height)
fun layoutFillHeight(height: Int) = LinearLayout.LayoutParams(0, height, 1f)
