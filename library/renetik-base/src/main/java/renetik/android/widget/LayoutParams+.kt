package renetik.android.widget

import android.view.Gravity.CENTER
import android.view.Gravity.NO_GRAVITY
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import renetik.android.core.extensions.content.dpToPixel
import renetik.android.core.CSApplication.Companion.app

val layoutMatch get() = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
val layoutMatchCenter get() = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, CENTER)
val layoutMatchWrap get() = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
val layoutMatchWrapCenter get() = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, CENTER)
val layoutWrapMatchCenter get() = FrameLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT, CENTER)
val layoutMatchFill get() = LinearLayout.LayoutParams(MATCH_PARENT, 0, 1f)
val layoutWrapFill get() = LinearLayout.LayoutParams(WRAP_CONTENT, 0, 1f)
val layoutWrap get() = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
fun layoutWrap(gravity: Int) = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, gravity)
val layoutWrapMatch get() = FrameLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
val layoutFillMatch get() = LinearLayout.LayoutParams(0, MATCH_PARENT, 1f)
val layoutFillWrap get() = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)

fun layoutWrapHeight(dpHeight: Int, gravity: Int? = null) =
    layoutWrapHeight(dpHeight.toFloat(), gravity)

fun layoutWrapHeight(dpHeight: Float, gravity: Int? = null) =
    FrameLayout.LayoutParams(WRAP_CONTENT,
        app.dpToPixel(dpHeight), gravity ?: NO_GRAVITY)

fun layoutMatchHeight(dpHeight: Int, gravity: Int? = null) =
    layoutMatchHeight(dpHeight.toFloat(), gravity)

fun layoutMatchHeight(dpHeight: Float, gravity: Int? = null) =
    FrameLayout.LayoutParams(MATCH_PARENT,
        app.dpToPixel(dpHeight), gravity ?: NO_GRAVITY)

fun layoutMatchHeightPx(pixelHeight: Int) =
    FrameLayout.LayoutParams(MATCH_PARENT, pixelHeight)

fun layoutMatchHeight(dpHeight: Int) = layoutMatchHeight(dpHeight.toFloat())
fun layoutWidthHeight(dpWidth: Int, dpHeight: Int) =
    layoutWidthHeight(dpWidth.toFloat(), dpHeight.toFloat())

fun layoutWidthHeight(dpWidth: Float, dpHeight: Float) =
    FrameLayout.LayoutParams(app.dpToPixel(dpWidth), app.dpToPixel(dpHeight))