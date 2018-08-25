package cs.android.extensions

import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) = apply {
    val out = FileOutputStream(this)
    bitmap.compress(format, quality, out)
    out.flush()
    out.close()
}