package renetik.android.imaging.extensions

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.DiskCacheStrategy.ALL
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.signature.ObjectKey
import renetik.android.event.property.CSProperty
import renetik.android.event.property.action
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistrationsList
import java.io.File

fun <T : ImageView> T.image(
    @DrawableRes resourceId: Int?) = apply {
    resourceId?.let {
        Glide.with(this@image).load(resourceId)
            .fitCenter().diskCacheStrategy(ALL)
            .into(this)
    } ?: run { setImageDrawable(null) }
}

fun <T : ImageView> T.image(file: File) = image(file, null)

fun <T : ImageView> T.image(
    file: File, borderWidth: Float, radius: Float = 5f, color: Int = Color.DKGRAY) =
    image(file, BorderBitmapTransformation(borderWidth, radius, color))

fun <T> ImageView.image(
    property: CSProperty<T>,
    borderWidth: Float = 1f, radius: Float = 2f, color: Int = Color.BLACK,
    valueToImage: (T) -> File): CSRegistration {
    val registration = CSRegistrationsList(this)
    registration.register(property.action {
        image(valueToImage(property.value), borderWidth, radius, color)
    })
    return registration
}

fun <T> ImageView.image(
    property: CSProperty<T>,
    valueToImage: (T) -> File): CSRegistration {
    val registration = CSRegistrationsList(this)
    registration.register(property.action {
        image(valueToImage(property.value))
    })
    return registration
}

private fun <T : ImageView> T.image(
    file: File, transformation: Transformation<Bitmap>? = null) = apply {
    val builder = Glide.with(context).asBitmap().load(file)
        .signature(ObjectKey(file.lastModified()))
        .fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE)
    transformation?.let { builder.apply(bitmapTransform(it)) }
    builder.into(this)
}