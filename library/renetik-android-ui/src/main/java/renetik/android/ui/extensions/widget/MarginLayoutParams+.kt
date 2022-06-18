package renetik.android.ui.extensions.widget

import android.view.ViewGroup

fun ViewGroup.MarginLayoutParams.margin(
    left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) =
    apply { setMargins(left, top, right, bottom) }

fun ViewGroup.MarginLayoutParams.margin(value: Int) =
    apply { setMargins(value, value, value, value) }

fun ViewGroup.MarginLayoutParams.verticalMargin(value: Int) =
    apply { setMargins(leftMargin, value, rightMargin, value) }

fun ViewGroup.MarginLayoutParams.horizontalMargin(value: Int) =
    apply { setMargins(value, topMargin, value, bottomMargin) }