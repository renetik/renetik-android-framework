package renetik.android.ui.extensions.widget

import android.content.res.ColorStateList
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.ColorInt
import renetik.android.event.property.CSProperty
import renetik.android.event.property.action
import renetik.android.event.registration.CSRegistration
import java.io.File

fun <T : ImageView> T.iconTint(@ColorInt color: Int) =
    apply { imageTintList = ColorStateList.valueOf(color) }

fun <T : ImageView> T.image(file: File) = apply { setImageURI(Uri.fromFile(file)) }

fun <T> ImageView.image(property: CSProperty<T>, valueToImage: (T) -> File)
        : CSRegistration = property.action { image(valueToImage(property.value)) }