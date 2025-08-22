package renetik.android.imaging.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri.fromFile
import renetik.android.core.java.io.createFileAndDirs
import java.io.File
import java.io.FileOutputStream

fun File.resizeImage(maxTargetWidth: Int, maxTargetHeight: Int) = apply {
    fromFile(this).resizeImage(maxTargetWidth, maxTargetHeight,
        FileOutputStream(this))
}

fun File.write(
    bitmap: Bitmap, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 75
) = apply {
    createFileAndDirs()
    outputStream().use {
        bitmap.compress(format, quality, it)
        it.flush()
    }
}

fun File.loadBitmap(mutable: Boolean = false): Bitmap? {
    val options = BitmapFactory.Options()
    options.inMutable = mutable
    return BitmapFactory.decodeFile(path, options)
}