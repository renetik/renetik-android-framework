package cs.android.extensions.view

import android.net.Uri.fromFile
import android.widget.ImageView
import androidx.core.graphics.drawable.DrawableCompat
import java.io.File

fun ImageView.image(file: File) = apply {
    setImageURI(fromFile(file))
//    setImageBitmap(decodeFile(file.path))
}

fun ImageView.image(resourceId: Int) = apply {
    setImageResource(resourceId)
}

fun ImageView.iconTint(color: Int) = apply {
    DrawableCompat.setTint(drawable, color)
}