package renetik.android.extensions.view

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout.LayoutParams
import renetik.android.extensions.toPixel
import renetik.android.base.application

val layoutMatch get() = LayoutParams(MATCH_PARENT, MATCH_PARENT)
val layoutMatchWrap get() = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
val layoutMatchFill get() = LayoutParams(MATCH_PARENT, 0, 1f)
val layoutWrap get() = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
val layoutWrapMatch get() = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
val layoutFillMatch get() = LayoutParams(0, MATCH_PARENT, 1f)

fun layoutWrapHeight(dpHeight: Float) = LayoutParams(WRAP_CONTENT, application.toPixel(dpHeight))
fun layoutWrapHeight(dpHeight: Int) = layoutWrapHeight(dpHeight.toFloat())
fun layoutMatchHeight(dpHeight: Float) = LayoutParams(MATCH_PARENT, application.toPixel(dpHeight))
fun layoutMatchHeight(dpHeight: Int) = layoutMatchHeight(dpHeight.toFloat())
fun layoutWidthHeight(dpWidth: Int, dpHeight: Int) = layoutWidthHeight(dpWidth.toFloat(), dpHeight.toFloat())
fun layoutWidthHeight(dpWidth: Float, dpHeight: Float) = LayoutParams(application.toPixel(dpWidth)
        , application.toPixel(dpHeight))

fun LayoutParams.margin(left: Int, top: Int, right: Int, bottom: Int) = apply { setMargins(left, top, right, bottom) }
fun LayoutParams.margin(value: Int) = apply { setMargins(value, value, value, value) }
fun LayoutParams.verticalMargin(value: Int) = apply { setMargins(leftMargin, value, rightMargin, value) }
fun LayoutParams.horizontalMargin(value: Int) = apply { setMargins(value, topMargin, value, bottomMargin) }