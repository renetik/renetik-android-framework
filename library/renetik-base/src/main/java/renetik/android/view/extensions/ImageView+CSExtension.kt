package renetik.android.view.extensions

import android.content.res.ColorStateList
import android.graphics.BitmapFactory.decodeFile
import android.widget.ImageView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import renetik.android.java.common.tryAndError
import java.io.File

fun <T : ImageView> T.iconTint(color: Int) = apply { imageTintList = ColorStateList.valueOf(color) }
fun <T : ImageView> T.image(resourceId: Int) = apply { setImageResource(resourceId) }
fun <T : ImageView> T.image(file: File) = apply {
    doAsync {
        tryAndError { decodeFile(file.absolutePath) }
                ?.let { bitmap -> uiThread { setImageBitmap(bitmap) } }
    }
}