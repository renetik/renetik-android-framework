package renetik.android.core.android.graphics

import android.graphics.Paint

inline fun Paint(function: (Paint).() -> Unit): Paint =
    Paint(Paint.ANTI_ALIAS_FLAG).also(function)

inline fun Paint.clone(function: (Paint).() -> Unit): Paint =
    Paint(this).also(function)
