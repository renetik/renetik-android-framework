package renetik.android.framework.view

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import renetik.android.event.registration.CSRegistration
import renetik.android.event.property.CSEventProperty
import java.io.File

fun <T : ImageView> T.image(
    @DrawableRes resourceId: Int?,
    useAndroidSdk: Boolean = false) = apply {
    resourceId?.let {
        if (useAndroidSdk) setImageResource(it)
        else Glide.with(context).load(it).into(this)
    } ?: run { setImageDrawable(null) }
}

fun <T : ImageView> T.image(file: File, useAndroidSdk: Boolean = false) = apply {
    if (useAndroidSdk)
        setImageURI(Uri.fromFile(file));
    else
    //Cache invalidation https://muyangmin.github.io/glide/doc/caching.html#cache-invalidation
        Glide.with(context).load(file)
            .signature(ObjectKey(file.lastModified())).into(this)
}

fun <T> ImageView.image(property: CSEventProperty<T>,
                        useAndroidSdk: Boolean = false,
                        valueToImage: (T) -> File
): CSRegistration {
    fun updateImage() = image(valueToImage(property.value), useAndroidSdk)
    updateImage()
    return property.onChange { updateImage() }
}