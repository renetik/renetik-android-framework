package renetik.android.imaging.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri.fromFile
import renetik.android.java.extensions.createFileAndDirs
import renetik.android.logging.CSLog.logInfo
import java.io.File
import java.io.FileOutputStream


fun File.resizeImage(maxTargetWidth: Int, maxTargetHeight: Int) = apply {
    fromFile(this).resizeImage(maxTargetWidth, maxTargetHeight, FileOutputStream(this))
}

fun File.write(
    bitmap: Bitmap, format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
    , quality: Int = 100
) = apply {
    createFileAndDirs()
    outputStream().use { out ->
        bitmap.compress(format, quality, out)
        out.flush()
    }
    logInfo("$this saved:${exists()}")
}

fun File.loadBitmap(mutable: Boolean = false): Bitmap? {
    val options = BitmapFactory.Options()
    options.inMutable = mutable
    return BitmapFactory.decodeFile(path, options)
}


//fun File.resizeImage(maxTargetWidth: Int, maxTargetHeight: Int, context: Context) {
//    val futureBitmap = Glide.with(context).asBitmap().load(this).apply(RequestOptions
//            .overrideOf(maxTargetWidth, maxTargetHeight).centerInside().diskCacheStrategy(NONE))
//            .submit(SIZE_ORIGINAL, SIZE_ORIGINAL)
//    tryAndError(FileNotFoundException::class) {
//        FileOutputStream(this@resizeImage).use { futureBitmap.get().compress(JPEG, 80, it) }
//    }
//}

