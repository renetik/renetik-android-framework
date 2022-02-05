package renetik.android.widget

//import coil.load
import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.event.property.CSEventProperty
import java.io.File

fun <T : ImageView> T.iconTint(@ColorInt color: Int) =
    apply { imageTintList = ColorStateList.valueOf(color) }

fun <T : ImageView> T.image(@DrawableRes resourceId: Int) = apply {
//    load(resourceId)

    // Should test again was making horrible note sheet images
    // Glide.with(context).load(resourceId).into(this)
    setImageResource(resourceId)
}

fun <T : ImageView> T.image(file: File) = apply {
//    load(file)
//    setImageBitmap(decodeFile(file.absolutePath))

    //Cache invalidation https://muyangmin.github.io/glide/doc/caching.html#cache-invalidation
    Glide.with(context).load(file)
        .signature(ObjectKey(file.lastModified())).into(this)
}

fun <T> ImageView.image(property: CSEventProperty<T>,
                        valueToImage: (T) -> File
): CSEventRegistration {
    fun updateImage() = image(valueToImage(property.value))
    updateImage()
    return property.onChange { updateImage() }
}