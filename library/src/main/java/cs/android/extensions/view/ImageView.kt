package cs.android.extensions.view

import android.net.Uri.fromFile
import android.widget.ImageView
import androidx.core.graphics.drawable.DrawableCompat
import com.bumptech.glide.Glide
import java.io.File

fun ImageView.image(file: File) = apply {
    setImageURI(fromFile(file))
}

fun ImageView.image(url: String) = apply {
    Glide.with(this).load(url).into(this);
}

fun ImageView.image(resourceId: Int) = apply {
    setImageResource(resourceId)
}

fun ImageView.iconTint(color: Int) = apply {
    DrawableCompat.setTint(drawable, color)
}