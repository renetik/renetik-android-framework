package renetik.android.imaging.extensions

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.Bitmap.createScaledBitmap
import android.graphics.Canvas
import android.graphics.Color.alpha
import android.graphics.Color.argb
import android.graphics.Color.blue
import android.graphics.Color.green
import android.graphics.Color.red
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import java.io.File

fun Bitmap.destruct() {
    if (!isRecycled) recycle()
}

fun Bitmap.scale(
    maxTargetWidth: Int, maxTargetHeight: Int = maxTargetWidth
): Bitmap {
    val aspectRatio = width.toFloat() / height.toFloat()

    val targetWidth: Int = if (width > height)
        maxTargetWidth else (maxTargetHeight * aspectRatio).toInt()

    val targetHeight: Int = if (width > height)
        (maxTargetWidth / aspectRatio).toInt() else maxTargetHeight

    return createScaledBitmap(
        this, targetWidth, targetHeight, true
    ).also { this.destruct() }
}

fun Bitmap.toMonochrome(): Bitmap {
    val grayscaleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(grayscaleBitmap)
    val paint = Paint()
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(0f)
    val filter = ColorMatrixColorFilter(colorMatrix)
    paint.colorFilter = filter
    canvas.drawBitmap(this, 0f, 0f, paint)
    return grayscaleBitmap
}

fun Bitmap.toMonochromeManual(): Bitmap {
    val monochromeBitmap = copy(Bitmap.Config.ARGB_8888, true)
    for (y in 0 until monochromeBitmap.height) {
        for (x in 0 until monochromeBitmap.width) {
            val pixel = monochromeBitmap.getPixel(x, y)
            val gray = (0.299 * red(pixel) + 0.587 * green(pixel) + 0.114 * blue(pixel)).toInt()
            monochromeBitmap.setPixel(x, y, argb(alpha(pixel), gray, gray, gray))
        }
    }
    return monochromeBitmap
}


fun Bitmap.copy(): Bitmap? = config?.let { copy(it, isMutable) }

fun Bitmap.saveTo(file: File, format: CompressFormat = JPEG, quality: Int = 80) =
    apply { file.write(this, format, quality) }