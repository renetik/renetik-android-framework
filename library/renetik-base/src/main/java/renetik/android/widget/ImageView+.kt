package renetik.android.widget

import android.content.res.ColorStateList
import android.graphics.BitmapFactory.decodeFile
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import renetik.android.framework.common.catchAllErrorReturnNull
import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.task.CSBackground.background
import renetik.kotlin.later
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

fun <T> ImageView.image(property: CSEventProperty<T>,
                        valueToImage: (T) -> File
): CSEventRegistration {
    fun updateImage() = image(valueToImage(property.value))
    updateImage()
    return property.onChange { updateImage() }
}