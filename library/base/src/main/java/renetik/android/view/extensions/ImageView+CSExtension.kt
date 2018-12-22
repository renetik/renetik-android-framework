package renetik.android.view.extensions

import android.content.res.ColorStateList
import android.widget.ImageView

fun <T : ImageView> T.iconTint(color: Int) = apply {
    imageTintList = ColorStateList.valueOf(color)
}