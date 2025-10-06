package renetik.android.imaging.extensions

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.action
import java.io.File

val instance = CSGlideImaging()

fun <T : ImageView> T.imageGlide(@DrawableRes resourceId: Int?): T = apply {
    instance.load(this, resourceId)
}

fun <T : ImageView> T.drawableGlide(drawable: Drawable?): T = apply {
    instance.load(this, drawable)
}

fun <T : ImageView> T.imageGlide(file: File) = apply {
    instance.load(this, file)
}

fun <T : ImageView> T.imageGlide(bitmap: Bitmap?) = apply {
    instance.load(this, bitmap)
}

fun <T : ImageView> T.imageGlide(uri: Uri?) = apply {
    instance.load(this, uri)
}

@JvmName("imagePropertyResource")
fun <T> ImageView.image(property: CSHasChangeValue<T>, resource: (T) -> Int?)
        : CSRegistration = property.action { imageGlide(resource(property.value)) }

@JvmName("imagePropertyBitmap")
fun <T> ImageView.image(property: CSHasChangeValue<T>, bitmap: (T) -> Bitmap?)
        : CSRegistration = property.action { imageGlide(bitmap(property.value)) }

@JvmName("imagePropertyFile")
fun <T> ImageView.image(property: CSHasChangeValue<T>, file: (T) -> File)
        : CSRegistration = property.action { imageGlide(file(property.value)) }