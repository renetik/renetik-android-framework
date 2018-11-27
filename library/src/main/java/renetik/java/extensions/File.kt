package renetik.java.extensions

import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

fun File.write(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) = apply {
    createNewFile()
    outputStream().use { out ->
        bitmap.compress(format, quality, out)
        out.flush()
    }
}

fun File.write(text: String) = apply {
    createNewFile()
    writeText(text)
}

fun File.recreate() = apply {
    deleteRecursively()
    mkdirs()
}