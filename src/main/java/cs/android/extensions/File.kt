package cs.android.extensions

import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

fun File.write(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) = apply {
    val out = FileOutputStream(this)
    bitmap.compress(format, quality, out)
    out.flush()
    out.close()
}

fun File.write(text: String) = apply {
    createNewFile()
    writeText(text)
}

fun File.recreate() = apply {
    deleteRecursively()
    mkdirs()
}