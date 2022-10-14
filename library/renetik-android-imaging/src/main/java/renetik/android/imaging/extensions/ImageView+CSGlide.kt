package renetik.android.imaging.extensions

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy.ALL
import com.bumptech.glide.signature.ObjectKey
import renetik.android.event.property.CSProperty
import renetik.android.event.property.action
import renetik.android.event.registration.*
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.ui.extensions.view.hasSize
import java.io.File

//fun <T : ImageView> T.image(@DrawableRes resourceId: Int?) = apply {
//    resourceId?.let(this::setImageResource) ?: run { setImageDrawable(null) }
//}

fun <T : ImageView> T.image(
    parent: CSHasRegistrations, @DrawableRes resourceId: Int?) = apply {
//    resourceId?.let {
//        hasSize(parent) {
//            Glide.with(this@image).load(resourceId)
//                .fitCenter().diskCacheStrategy(ALL).override(width, height)
//                .into(this)
//        }
//    } ?: run { setImageDrawable(null) }
    parent.register(image(resourceId))
}

fun <T : ImageView> T.image(@DrawableRes resourceId: Int?): CSRegistration? {
    resourceId?.let {
        val registration = CSRegistrationsList(this)
        registration.register(hasSize {
            val request = Glide.with(this@image).load(resourceId)
                .fitCenter().diskCacheStrategy(ALL).override(width, height)
                .into(this).request
            registration.register(CSRegistration { request?.clear() })
        })
        return registration
    } ?: run { setImageDrawable(null) }
    return null
}

//// https://muyangmin.github.io/glide/doc/caching.html#cache-invalidation
//fun <T : ImageView> T.image(parent: CSHasRegistrations, file: File) = apply {
//    hasSize(parent) {
//        Glide.with(this@image).load(file).signature(ObjectKey(file.lastModified()))
//            .fitCenter().diskCacheStrategy(ALL).override(width, height)
//            .into(this)
//    }
//}

fun <T : ImageView> T.image(parent: CSHasRegistrations, file: File) = apply {
    parent.register(image(file))
}


// https://muyangmin.github.io/glide/doc/caching.html#cache-invalidation
fun <T : ImageView> T.image(file: File): CSRegistration {
    val registration = CSRegistrationsMap(this)
    registration.register(hasSize {
        val request = Glide.with(this@image).load(file)
            .signature(ObjectKey(file.lastModified()))
            .fitCenter().diskCacheStrategy(ALL)
            .override(width, height).into(this).request
        registration.register(CSRegistration { request?.clear() })
    })
    return registration
}

// TODO!!!: There is leak issue when using this form registerUntilHide
//fun <T> ImageView.image(
//    parent: CSHasRegistrations, property: CSProperty<T>,
//    valueToImage: (T) -> File): CSRegistration =
//    property.action { image(parent, valueToImage(property.value)) }


fun <T> ImageView.image(
    property: CSProperty<T>,
    valueToImage: (T) -> File): CSRegistration {
    val registration = CSRegistrationsMap(this)
    registration.register(property.action {
        image(registration, valueToImage(property.value))
    })
    return registration
}