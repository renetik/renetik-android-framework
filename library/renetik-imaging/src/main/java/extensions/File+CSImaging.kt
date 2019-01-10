package renetik.android.imaging.extensions

import android.net.Uri.fromFile
import java.io.File
import java.io.FileOutputStream

fun File.resizeImage(maxTargetWidth: Int, maxTargetHeight: Int) = apply {
    fromFile(this).resizeImage(maxTargetWidth, maxTargetHeight, FileOutputStream(this))
}

//fun File.resizeImage(maxTargetWidth: Int, maxTargetHeight: Int, context: Context) {
//    val futureBitmap = Glide.with(context).asBitmap().load(this).apply(RequestOptions
//            .overrideOf(maxTargetWidth, maxTargetHeight).centerInside().diskCacheStrategy(NONE))
//            .submit(SIZE_ORIGINAL, SIZE_ORIGINAL)
//    tryAndError(FileNotFoundException::class) {
//        FileOutputStream(this@resizeImage).use { futureBitmap.get().compress(JPEG, 80, it) }
//    }
//}

