package renetik.android.ui.extensions.widget

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.net.Uri.fromFile
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import renetik.android.core.kotlin.unexpected
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.action
import java.io.File

class AndroidImaging() : CSImaging {
    companion object {
        fun initialize() = AndroidImaging().also { CSImaging.instance = it }
    }

    override fun load(view: ImageView, uri: Uri?): Unit = unexpected()
    override fun load(view: ImageView, file: File) = view.setImageURI(fromFile(file))
    override fun load(view: ImageView, bitmap: Bitmap?) = view.setImageBitmap(bitmap)
    override fun load(view: ImageView, drawable: Drawable?) = view.setImageDrawable(drawable)
    override fun load(view: ImageView, resourceId: Int?) =
        resourceId?.let(view::setImageResource) ?: view.setImageDrawable(null)
}

interface CSImaging {
    companion object {
        lateinit var instance: CSImaging
    }

    fun load(view: ImageView, uri: Uri?)
    fun load(view: ImageView, file: File)
    fun load(view: ImageView, bitmap: Bitmap?)
    fun load(view: ImageView, drawable: Drawable?)
    fun load(view: ImageView, @DrawableRes resourceId: Int?)
}

fun <T : ImageView> T.image(@DrawableRes resourceId: Int?): T = apply {
    CSImaging.instance.load(this, resourceId)
}

fun <T : ImageView> T.drawable(drawable: Drawable?): T = apply {
    CSImaging.instance.load(this, drawable)
}

fun <T : ImageView> T.image(file: File) = apply {
    CSImaging.instance.load(this, file)
}

fun <T : ImageView> T.image(bitmap: Bitmap?) = apply {
    CSImaging.instance.load(this, bitmap)
}

fun <T : ImageView> T.image(uri: Uri?) = apply {
    CSImaging.instance.load(this, uri)
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