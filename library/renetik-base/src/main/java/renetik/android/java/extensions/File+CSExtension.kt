package renetik.android.java.extensions

import android.graphics.Bitmap
import java.io.File
import java.io.File.createTempFile
import java.util.*


fun File.createNewFileAndDirs() {
    parentFile.mkdirs()
    createNewFile()
}

fun File.write(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) = apply {
    createNewFileAndDirs()
    outputStream().use { out ->
        bitmap.compress(format, quality, out)
        out.flush()
    }
}

fun File.write(text: String) = apply {
    createNewFileAndDirs()
    writeText(text)
}

fun File.recreate() = apply {
    deleteRecursively()
    mkdirs()
}

fun File.createDatedFile(extension: String): File {
    mkdirs()
    return createTempFile(Date().format("yyyy-MM-dd_HH-mm-ss"), ".$extension", this)
}

