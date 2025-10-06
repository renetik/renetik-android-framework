package renetik.android.imaging.extensions

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Color.DKGRAY
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.signature.ObjectKey
import renetik.android.core.kotlin.changeIf
import renetik.android.core.kotlin.changeIfNotNull
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.action
import renetik.android.ui.extensions.view.onHasSize
import renetik.android.ui.extensions.widget.image
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
fun <T> ImageView.imageGlide(property: CSHasChangeValue<T>, resource: (T) -> Int?)
        : CSRegistration = property.action { imageGlide(resource(property.value)) }

@JvmName("imagePropertyBitmap")
fun <T> ImageView.imageGlide(property: CSHasChangeValue<T>, bitmap: (T) -> Bitmap?)
        : CSRegistration = property.action { imageGlide(bitmap(property.value)) }

@JvmName("imagePropertyFile")
fun <T> ImageView.imageGlide(property: CSHasChangeValue<T>, file: (T) -> File)
        : CSRegistration = property.action { imageGlide(file(property.value)) }

fun <T : ImageView> T.imageGlide(
    url: String, progressView: View? = null,
    errorDrawable: Int? = null,
    transformation: Transformation<Bitmap>? = null
) {
    setImageDrawable(null)
    Glide.with(this).load(url).fitCenter()
        .changeIfNotNull(progressView) {
            addListener(CSGlideProgressListener(it,
                onFailed = { image(errorDrawable) }))
        }
        .changeIf(progressView == null) {
            addListener(DrawableRequestAdapter(
                onFailed = { image(errorDrawable) }))
        }
        .changeIfNotNull(transformation) {
            apply(bitmapTransform(it))
        }.into(this)
}

fun <T : ImageView> T.imageGlide(parent: CSHasRegistrations, file: File)
        : CSRegistration? = imageGlide(parent, file, null)

fun <T : ImageView> T.imageGlide(
    parent: CSHasRegistrations, file: File, borderWidth: Float,
    radius: Float = 5f, color: Int = DKGRAY
): CSRegistration? =
    imageGlide(parent, file, BorderBitmapTransformation(borderWidth, radius, color))

fun <T> ImageView.imageGlide(
    parent: CSHasRegistrations, property: CSProperty<T>,
    borderWidth: Float = 1f, radius: Float = 2f, color: Int = Color.BLACK,
    file: (T) -> File
): CSRegistration =
    property.action { imageGlide(parent, file(property.value), borderWidth, radius, color) }

fun <T> ImageView.imageGlide(
    parent: CSHasRegistrations,
    property: CSProperty<T>,
    valueToImage: (T) -> File
): CSRegistration =
    property.action { imageGlide(parent, valueToImage(property.value)) }

private fun <T : ImageView> T.imageGlide(
    parent: CSHasRegistrations, file: File,
    transformation: Transformation<Bitmap>? = null
): CSRegistration? = onHasSize(parent) {
    val builder = Glide.with(this).asBitmap().load(file)
        .override(width, height)
        .signature(ObjectKey(file.lastModified()))
        .fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE)
    transformation?.let { builder.apply(bitmapTransform(it)) }
    builder.into(this)
}