package renetik.android.ui.extensions.widget

import android.content.res.ColorStateList
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSHasChangeValue.Companion.action
import renetik.android.event.registration.CSRegistration
import java.io.File

fun <T : ImageView> T.image(@DrawableRes resourceId: Int?): T = apply {
    resourceId?.let { setImageResource(it) } ?: run { setImageDrawable(null) }
}

@JvmName("imagePropertyResource")
fun <T> ImageView.image(property: CSProperty<T>, resource: (T) -> Int?)
        : CSRegistration = property.action { image(resource(property.value)) }

fun <T : ImageView> T.image(file: File) = apply { setImageURI(Uri.fromFile(file)) }

@JvmName("imagePropertyFile")
fun <T> ImageView.image(property: CSProperty<T>, file: (T) -> File)
        : CSRegistration = property.action { image(file(property.value)) }

fun <T : ImageView> T.iconTint(@ColorInt color: Int) =
    apply { imageTintList = ColorStateList.valueOf(color) }