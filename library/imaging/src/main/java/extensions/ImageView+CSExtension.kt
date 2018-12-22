package renetik.android.imaging.extensions

import android.content.res.ColorStateList
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.File

fun <T : ImageView> T.image(file: File) = apply {
    Glide.with(this).load(file).into(this)
}

fun <T : ImageView> T.image(url: String) = apply {
    Glide.with(this).load(url).into(this)
}

fun <T : ImageView> T.image(resourceId: Int) = apply {
    Glide.with(this).load(resourceId).into(this)
}