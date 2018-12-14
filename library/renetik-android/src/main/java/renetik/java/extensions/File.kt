package renetik.java.extensions

import android.graphics.Bitmap
import android.os.Environment.DIRECTORY_PICTURES
import renetik.android.model.application
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale.US

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

fun File.createDatedFile(extension: String): File {
    val timeStamp = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", US).format(Date())
    return File.createTempFile(timeStamp, ".$extension", this)
}