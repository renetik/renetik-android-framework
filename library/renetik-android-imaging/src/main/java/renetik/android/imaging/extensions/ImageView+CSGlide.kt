package renetik.android.imaging.extensions

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy.ALL
import com.bumptech.glide.signature.ObjectKey
import renetik.android.event.property.CSProperty
import renetik.android.event.property.action
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.ui.extensions.view.hasSize
import java.io.File

fun <T : ImageView> T.image(@DrawableRes resourceId: Int?) = apply {
    resourceId?.let(this::setImageResource) ?: run { setImageDrawable(null) }
}

fun <T : ImageView> T.image(
    parent: CSHasRegistrations, @DrawableRes resourceId: Int?) = apply {
    resourceId?.let {
        hasSize(parent) {
            Glide.with(this@image).load(resourceId)
                .fitCenter().diskCacheStrategy(ALL).override(width, height)
                .into(this)
        }
    } ?: run { setImageDrawable(null) }
}

// https://muyangmin.github.io/glide/doc/caching.html#cache-invalidation
fun <T : ImageView> T.image(parent: CSHasRegistrations, file: File) = apply {
    hasSize(parent) {
        Glide.with(this@image).load(file).signature(ObjectKey(file.lastModified()))
            .fitCenter().diskCacheStrategy(ALL).override(width, height)
            .into(this)
    }
}

fun <T> ImageView.image(
    parent: CSHasRegistrations, property: CSProperty<T>, valueToImage: (T) -> File)
        : CSRegistration = property.action { image(parent, valueToImage(property.value)) }