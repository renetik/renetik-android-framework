package renetik.android.widget

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.annotation.ColorInt

fun <T : ImageView> T.iconTint(@ColorInt color: Int) =
    apply { imageTintList = ColorStateList.valueOf(color) }