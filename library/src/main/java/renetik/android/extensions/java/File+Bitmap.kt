@file:Suppress("DEPRECATION")

package renetik.android.extensions.java

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.Bitmap.createBitmap
import android.graphics.Bitmap.createScaledBitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.decodeStream
import android.graphics.Matrix
import android.graphics.RectF
import android.media.ExifInterface
import android.media.ExifInterface.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy.NONE
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import renetik.java.extensions.close
import renetik.java.lang.tryAndError
import java.io.*
import java.lang.Integer.max

fun File.resizeImage(maxTargetWidth: Int, maxTargetHeight: Int, context: Context) {
    Glide.with(context).asBitmap().load(this).apply(RequestOptions
            .overrideOf(maxTargetWidth, maxTargetHeight).centerInside()
            .diskCacheStrategy(NONE)).into(object : SimpleTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            tryAndError(FileNotFoundException::class) {
                val out = FileOutputStream(this@resizeImage)
                resource.compress(JPEG, 80, out)
                close(out)
            }
        }
    })
}

fun File.resizeImage(maxTargetWidth: Int, maxTargetHeight: Int) {
    tryAndError {
        var input: InputStream = FileInputStream(this)
        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        decodeStream(input, null, options)
        close(input)

        val inWidth = options.outWidth
        val inHeight = options.outHeight

        input = FileInputStream(this)
        options = BitmapFactory.Options()
        options.inSampleSize = max(inWidth / maxTargetWidth, inHeight / maxTargetHeight)
        val roughBitmap = decodeStream(input, null, options)

        val m = Matrix()
        val inRect = RectF(0f, 0f, roughBitmap!!.getWidth().toFloat(), roughBitmap.getHeight().toFloat())
        val outRect = RectF(0f, 0f, maxTargetWidth.toFloat(), maxTargetHeight.toFloat())
        m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER)
        val values = FloatArray(9)
        m.getValues(values)

        var resizedBitmap = createScaledBitmap(roughBitmap,
                (roughBitmap.getWidth() * values[0]).toInt(),
                (roughBitmap.getHeight() * values[4]).toInt(), true)

        resizedBitmap = this.rotateBitmap(resizedBitmap)
        val out = FileOutputStream(this)
        resizedBitmap.compress(JPEG, 80, out)
        close(out)
    }
}


fun File.rotateBitmap(bitmap: Bitmap): Bitmap {
    tryAndError {
        val orientation = ExifInterface(this.absolutePath).getAttributeInt(TAG_ORIENTATION, 1)
        val matrix = Matrix()
        if (orientation == ORIENTATION_NORMAL) return@tryAndError bitmap
        if (orientation == ORIENTATION_FLIP_HORIZONTAL) matrix.setScale(-1f, 1f)
        else if (orientation == ORIENTATION_ROTATE_180) matrix.setRotate(180f)
        else if (orientation == ORIENTATION_FLIP_VERTICAL) {
            matrix.setRotate(180f)
            matrix.postScale(-1f, 1f)
        } else if (orientation == ORIENTATION_TRANSPOSE) {
            matrix.setRotate(90f)
            matrix.postScale(-1f, 1f)
        } else if (orientation == ORIENTATION_ROTATE_90) {
            matrix.setRotate(90f)
        } else if (orientation == ORIENTATION_TRANSVERSE) {
            matrix.setRotate(-90f)
            matrix.postScale(-1f, 1f)
        } else if (orientation == ORIENTATION_ROTATE_270) matrix.setRotate(-90f)
        else return@tryAndError bitmap
        tryAndError(OutOfMemoryError::class) {
            createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                    .apply { bitmap.recycle() }
        }
    }
    return bitmap
}