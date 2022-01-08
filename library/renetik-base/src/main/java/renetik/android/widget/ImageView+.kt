package renetik.android.widget

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.MediaStoreSignature
//import coil.load
import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.event.property.CSEventProperty
import java.io.File

fun <T : ImageView> T.iconTint(@ColorInt color: Int) =
    apply { imageTintList = ColorStateList.valueOf(color) }

fun <T : ImageView> T.image(@DrawableRes resourceId: Int) = apply {
//    load(resourceId)
    Glide.with(context).load(resourceId).into(this)
//    setImageResource(resourceId)
}

fun <T : ImageView> T.image(file: File) = apply {
//    load(file)
    Glide.with(context).load(file)
            //Cache invalidation https://muyangmin.github.io/glide/doc/caching.html#cache-invalidation
        .signature(MediaStoreSignature("", file.lastModified(), 0)).into(this)
//    setImageBitmap(decodeFile(file.absolutePath))
}

fun <T> ImageView.image(property: CSEventProperty<T>,
                        valueToImage: (T) -> File
): CSEventRegistration {
    fun updateImage() = image(valueToImage(property.value))
    updateImage()
    return property.onChange { updateImage() }
}