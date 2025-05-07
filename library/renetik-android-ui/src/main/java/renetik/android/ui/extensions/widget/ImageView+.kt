package renetik.android.ui.extensions.widget

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri.fromFile
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.action
import java.io.File

fun <T : ImageView> T.image(@DrawableRes resourceId: Int?): T = apply {
    resourceId?.let(::setImageResource) ?: setImageDrawable(null)
}

fun <T : ImageView> T.drawable(drawable: Drawable?): T = apply {
    setImageDrawable(drawable)
}

fun <T : ImageView> T.image(file: File) = apply {
    setImageURI(fromFile(file))
}

fun <T : ImageView> T.image(bitmap: Bitmap?) = apply {
    setImageBitmap(bitmap)
}

val ImageView.hasImage get() = drawable != null

fun <T : ImageView> T.recycleBitmap() {
    (drawable as? BitmapDrawable)?.bitmap?.let { if (!it.isRecycled) it.recycle() }
}

@JvmName("imagePropertyResource")
fun <T> ImageView.image(property: CSHasChangeValue<T>, resource: (T) -> Int?)
        : CSRegistration = property.action { image(resource(property.value)) }

@JvmName("imagePropertyBitmap")
fun <T> ImageView.image(property: CSHasChangeValue<T>, bitmap: (T) -> Bitmap?)
        : CSRegistration = property.action { image(bitmap(property.value)) }

@JvmName("imagePropertyFile")
fun <T> ImageView.image(property: CSHasChangeValue<T>, file: (T) -> File)
        : CSRegistration = property.action { image(file(property.value)) }

fun <T : ImageView> T.iconTint(@ColorInt color: Int?) = apply {
    imageTintList = color?.let { ColorStateList.valueOf(it) }
}

@JvmName("imageNullableChildResource")
inline fun <T, V> ImageView.imageNullableChild(
    parent: CSHasChangeValue<T>,
    crossinline child: (T) -> CSHasChangeValue<V>?,
    noinline resource: (V?) -> Int
): CSRegistration {
    var childRegistration: CSRegistration? = null
    val parentRegistration = parent.action {
        childRegistration?.cancel()
        childRegistration = child(parent.value)?.let { image(it, resource) }
        if (childRegistration == null) image(resourceId = null)
    }
    return CSRegistration.CSRegistration(isActive = true, onCancel = {
        parentRegistration.cancel()
        childRegistration?.cancel()
    })
}

@JvmName("imageNullableChildBitmap")
inline fun <T, V> ImageView.imageNullableChild(
    parent: CSHasChangeValue<T>,
    crossinline child: (T) -> CSHasChangeValue<V>?,
    noinline bitmap: (V?) -> Bitmap
): CSRegistration {
    var childRegistration: CSRegistration? = null
    val parentRegistration = parent.action {
        childRegistration?.cancel()
        childRegistration = child(parent.value)?.let { image(it, bitmap) }
        if (childRegistration == null) image(bitmap = null)
    }
    return CSRegistration.CSRegistration(isActive = true, onCancel = {
        parentRegistration.cancel()
        childRegistration?.cancel()
    })
}