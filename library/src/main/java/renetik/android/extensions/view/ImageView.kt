package renetik.android.extensions.view

import android.net.Uri.fromFile
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.DrawableCompat
import com.bumptech.glide.Glide
import java.io.File

fun <T : ImageView> T.image(file: File) = apply {
    setImageURI(fromFile(file))
}

fun <T : ImageView> T.image(url: String) = apply {
    Glide.with(this).load(url).into(this);
}

fun <T : ImageView> T.image(resourceId: Int) = apply {
    setImageResource(resourceId)
}

fun <T : ImageView> T.iconTint(color: Int) = apply {
    DrawableCompat.setTint(drawable, color)
}