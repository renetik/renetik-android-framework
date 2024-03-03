package renetik.android.imaging.extensions

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Color.DKGRAY
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.signature.ObjectKey
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.action
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.action
import renetik.android.ui.extensions.view.onHasSize
import renetik.android.ui.protocol.CSViewInterface
import java.io.File

fun <T : ImageView> T.image(parent: CSViewInterface, file: File)
        : CSRegistration? = image(parent, file, null)

//TODO: inner has size registration shall be wrapped together with property registration
fun <T : ImageView> T.image(
    parent: CSViewInterface, file: File, borderWidth: Float,
    radius: Float = 5f, color: Int = DKGRAY
): CSRegistration? =
    image(parent, file, BorderBitmapTransformation(borderWidth, radius, color))

//TODO: inner has size registration shall be wrapped together with property registration
fun <T> ImageView.image(
    parent: CSViewInterface, property: CSProperty<T>,
    borderWidth: Float = 1f, radius: Float = 2f, color: Int = Color.BLACK,
    toImageFile: (T) -> File
): CSRegistration =
    property.action { image(parent, toImageFile(property.value), borderWidth, radius, color) }

//TODO: inner has size registration shall be wrapped together with property registration
fun <T> ImageView.image(
    parent: CSViewInterface,
    property: CSProperty<T>,
    valueToImage: (T) -> File
): CSRegistration =
    property.action { image(parent, valueToImage(property.value)) }

// hasSize & override(width, height) needed for WRAP_CONTENT
private fun <T : ImageView> T.image(
    parent: CSHasRegistrations, file: File,
    transformation: Transformation<Bitmap>? = null
): CSRegistration? =
    onHasSize(parent) {
        val builder = Glide.with(context).asBitmap().load(file)
            .override(width, height)
            .signature(ObjectKey(file.lastModified()))
            .fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE)
        transformation?.let { builder.apply(bitmapTransform(it)) }
        builder.into(this)
    }