package renetik.android.widget

import android.view.Gravity.CENTER
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import android.widget.LinearLayout.LayoutParams
import renetik.android.content.dpToPixel
import renetik.android.framework.CSApplication.Companion.application

val layoutMatch get() = LayoutParams(MATCH_PARENT, MATCH_PARENT)
val layoutMatchCenter get() = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, CENTER)
val layoutMatchWrap get() = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
val layoutMatchWrapCenter get() = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, CENTER)
val layoutWrapMatchCenter get() = FrameLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT, CENTER)
val layoutMatchFill get() = LayoutParams(MATCH_PARENT, 0, 1f)
val layoutWrap get() = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
fun layoutWrap(gravity: Int) = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, gravity)
val layoutWrapMatch get() = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
val layoutFillMatch get() = LayoutParams(0, MATCH_PARENT, 1f)
val layoutFillWrap get() = LayoutParams(0, WRAP_CONTENT, 1f)

fun layoutWrapHeight(dpHeight: Float) = LayoutParams(WRAP_CONTENT, application.dpToPixel(dpHeight))
fun layoutWrapHeight(dpHeight: Int) = layoutWrapHeight(dpHeight.toFloat())
fun layoutMatchHeight(dpHeight: Float) = LayoutParams(MATCH_PARENT, application.dpToPixel(dpHeight))
fun layoutMatchHeightPx(pixelHeight: Int) = LayoutParams(MATCH_PARENT, pixelHeight)
fun layoutMatchHeight(dpHeight: Int) = layoutMatchHeight(dpHeight.toFloat())
fun layoutWidthHeight(dpWidth: Int, dpHeight: Int) =
    layoutWidthHeight(dpWidth.toFloat(), dpHeight.toFloat())

fun layoutWidthHeight(dpWidth: Float, dpHeight: Float) =
    LayoutParams(application.dpToPixel(dpWidth), application.dpToPixel(dpHeight))

fun MarginLayoutParams.margin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) =
    apply { setMargins(left, top, right, bottom) }

fun MarginLayoutParams.margin(value: Int) = apply { setMargins(value, value, value, value) }
fun MarginLayoutParams.verticalMargin(value: Int) =
    apply { setMargins(leftMargin, value, rightMargin, value) }

fun MarginLayoutParams.horizontalMargin(value: Int) =
    apply { setMargins(value, topMargin, value, bottomMargin) }