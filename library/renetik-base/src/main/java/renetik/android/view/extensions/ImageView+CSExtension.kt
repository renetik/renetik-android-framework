package renetik.android.view.extensions

import android.content.res.ColorStateList
import android.graphics.BitmapFactory.decodeFile
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import renetik.android.framework.common.catchAllErrorReturnNull
import renetik.android.java.extensions.later
import renetik.android.task.CSBackground.background
import java.io.File

fun <T : ImageView> T.iconTint(@ColorInt color: Int) =
    apply { imageTintList = ColorStateList.valueOf(color) }

fun <T : ImageView> T.image(@DrawableRes resourceId: Int) =
    apply { setImageResource(resourceId) }

fun <T : ImageView> T.image(file: File) = apply {
    background {
        catchAllErrorReturnNull { decodeFile(file.absolutePath) }
            ?.let { bitmap -> later { setImageBitmap(bitmap) } }
    }
}