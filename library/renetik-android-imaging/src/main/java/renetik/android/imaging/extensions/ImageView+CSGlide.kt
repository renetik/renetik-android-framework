package renetik.android.imaging.extensions

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Color.DKGRAY
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.signature.ObjectKey
import renetik.android.core.kotlin.changeIf
import renetik.android.core.kotlin.changeIfNotNull
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.action
import renetik.android.ui.extensions.view.onHasSize
import renetik.android.ui.extensions.widget.image
import java.io.File

fun <T : ImageView> T.image(
    url: String, progress: View? = null,
    errorDrawable: Int? = null,
    transformation: Transformation<Bitmap>? = null
) {
    setImageDrawable(null)
    Glide.with(this).load(url).fitCenter()
        .changeIfNotNull(progress) { builder, progress ->
            builder.addListener(CSGlideProgressListener(progress,
                onFailed = { image(errorDrawable) }))
        }
        .changeIf(progress == null) { builder ->
            builder.addListener(DrawableRequestAdapter(
                onFailed = { image(errorDrawable) }))
        }
        .changeIfNotNull(transformation) { builder, transformation ->
            builder.apply(bitmapTransform(transformation))
        }.into(this)
}

fun <T : ImageView> T.image(parent: CSHasRegistrations, file: File)
        : CSRegistration? = image(parent, file, null)

//TODO: inner has size registration shall be wrapped together with property registration
fun <T : ImageView> T.image(
    parent: CSHasRegistrations, file: File, borderWidth: Float,
    radius: Float = 5f, color: Int = DKGRAY
): CSRegistration? =
    image(parent, file, BorderBitmapTransformation(borderWidth, radius, color))

//TODO: inner has size registration shall be wrapped together with property registration
fun <T> ImageView.image(
    parent: CSHasRegistrations, property: CSProperty<T>,
    borderWidth: Float = 1f, radius: Float = 2f, color: Int = Color.BLACK,
    file: (T) -> File
): CSRegistration =
    property.action { image(parent, file(property.value), borderWidth, radius, color) }

//TODO: inner has size registration shall be wrapped together with property registration
fun <T> ImageView.image(
    parent: CSHasRegistrations,
    property: CSProperty<T>,
    valueToImage: (T) -> File
): CSRegistration =
    property.action { image(parent, valueToImage(property.value)) }

// hasSize & override(width, height) needed for WRAP_CONTENT
private fun <T : ImageView> T.image(
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