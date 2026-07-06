package renetik.android.ui.view

import android.content.res.TypedArray
import androidx.annotation.StyleableRes
import androidx.core.content.res.getDimensionPixelSizeOrThrow

fun TypedArray.getDimensionPixelSize(@StyleableRes index: Int): Int? =
    runCatching { getDimensionPixelSizeOrThrow(index) }.getOrNull()