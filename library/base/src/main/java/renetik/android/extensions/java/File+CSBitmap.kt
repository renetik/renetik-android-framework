@file:Suppress("DEPRECATION")

package renetik.android.extensions.java

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.Bitmap.createBitmap
import android.graphics.Bitmap.createScaledBitmap
import android.graphics.BitmapFactory.Options
import android.graphics.BitmapFactory.decodeStream
import android.graphics.Matrix
import android.graphics.RectF
import android.net.Uri
import android.provider.MediaStore.Images.Media.ORIENTATION
import androidx.exifinterface.media.ExifInterface
import androidx.exifinterface.media.ExifInterface.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy.NONE
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import renetik.android.base.application
import renetik.java.lang.tryAndError
import renetik.java.lang.tryAndWarn
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.lang.Math.max


fun File.resizeImage(maxTargetWidth: Int, maxTargetHeight: Int, context: Context) {
    val futureBitmap = Glide.with(context).asBitmap().load(this).apply(RequestOptions
            .overrideOf(maxTargetWidth, maxTargetHeight).centerInside().diskCacheStrategy(NONE))
            .submit(SIZE_ORIGINAL, SIZE_ORIGINAL)
    tryAndError(FileNotFoundException::class) {
        FileOutputStream(this@resizeImage).use { futureBitmap.get().compress(JPEG, 80, it) }
    }
//    Glide.with(context).asBitmap().load(this).apply(RequestOptions
//            .overrideOf(maxTargetWidth, maxTargetHeight).centerInside()
//            .diskCacheStrategy(NONE)).into(object : SimpleTarget<Bitmap>() {
//        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//            tryAndError(FileNotFoundException::class) {
//                FileOutputStream(this@resizeImage).use { resource.compress(JPEG, 80, it) }
//            }
//        }
//    })
}

fun File.resizeImage(maxTargetWidth: Int, maxTargetHeight: Int, contentUri: Uri) = apply {
    tryAndError {
        val decodeBounds = Options()
        FileInputStream(this).use {
            decodeBounds.inJustDecodeBounds = true
            decodeStream(it, null, decodeBounds)
        }
        FileInputStream(this).use { input ->
            val options = Options()
            options.inSampleSize = max(decodeBounds.outWidth / maxTargetWidth,
                    decodeBounds.outHeight / maxTargetHeight)
            decodeStream(input, null, options)?.let { roughBitmap: Bitmap ->
                val scaledBitmap = scaleBitmap(roughBitmap, maxTargetWidth, maxTargetHeight)
                val rotatedBitmap = createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height,
                        createFixOrientationMatrix(contentUri), true)
                scaledBitmap.recycle()
                FileOutputStream(this).use {
                    rotatedBitmap.compress(JPEG, 80, it)
                    rotatedBitmap.recycle()
                }
            }
        }
    }
}

private fun scaleBitmap(roughBitmap: Bitmap, maxTargetWidth: Int, maxTargetHeight: Int): Bitmap {
    val matrix = Matrix()
    val inRect = RectF(0f, 0f, roughBitmap.width.toFloat(), roughBitmap.height.toFloat())
    val outRect = RectF(0f, 0f, maxTargetWidth.toFloat(), maxTargetHeight.toFloat())
    matrix.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER)
    val values = FloatArray(9)
    matrix.getValues(values)
    val scaled = createScaledBitmap(roughBitmap, (roughBitmap.width * values[0]).toInt(),
            (roughBitmap.height * values[4]).toInt(), true)
    roughBitmap.recycle()
    return scaled;
}


fun File.createFixOrientationMatrix(contentUri: Uri): Matrix {
    val matrix = Matrix()
    tryAndWarn {
        val orientation = ExifInterface(absolutePath).getAttributeInt(TAG_ORIENTATION, ORIENTATION_NORMAL)
        when (orientation) {
            ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
        }
        if (orientation == ORIENTATION_UNDEFINED) fixRotationByContentQuery(matrix, contentUri)
    }
    return matrix;
}

fun fixRotationByContentQuery(matrix: Matrix, photoUri: Uri) {
    val orientationColumn = arrayOf(ORIENTATION)
    val cur = application.contentResolver.query(photoUri, orientationColumn, null, null, null)
    cur?.use {
        var orientation = -1
        if (cur.moveToFirst()) orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]))
        matrix.postRotate(orientation.toFloat())
    }
}
