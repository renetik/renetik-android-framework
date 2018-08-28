package cs.android.extensions.view

import android.graphics.BitmapFactory.decodeFile
import android.net.Uri.fromFile
import android.widget.ImageView
import java.io.File

fun ImageView.image(file: File) = apply {
    setImageURI(fromFile(file))
//    setImageBitmap(decodeFile(file.path))
}

fun ImageView.image(resourceId: Int) = apply {
    setImageResource(resourceId)
}