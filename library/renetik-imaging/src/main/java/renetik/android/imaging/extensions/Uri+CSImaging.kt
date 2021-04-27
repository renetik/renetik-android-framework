package renetik.android.imaging.extensions

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.Bitmap.createBitmap
import android.graphics.BitmapFactory.Options
import android.graphics.BitmapFactory.decodeStream
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore.Images.Media.ORIENTATION
import androidx.exifinterface.media.ExifInterface
import androidx.exifinterface.media.ExifInterface.*
import renetik.android.framework.CSApplication.Companion.application
import renetik.android.extensions.openInputStream
import renetik.android.framework.common.catchAllError
import java.io.OutputStream
import kotlin.math.max

fun Uri.resizeImage(maxTargetWidth: Int, maxTargetHeight: Int, output: OutputStream) = apply {
    catchAllError {
        val decodeBounds = Options()
        application.openInputStream(this).use {
            decodeBounds.inJustDecodeBounds = true
            decodeStream(it, null, decodeBounds)
        }
        application.openInputStream(this).use { input ->
            val options = Options()
            options.inSampleSize = max(
                decodeBounds.outWidth / maxTargetWidth,
                decodeBounds.outHeight / maxTargetHeight
            )
            decodeStream(input, null, options)?.let { roughBitmap: Bitmap ->
                val scaledBitmap = roughBitmap.scale(maxTargetWidth, maxTargetHeight)
                val rotatedBitmap = createBitmap(
                    scaledBitmap, 0, 0,
                    scaledBitmap.width, scaledBitmap.height,
                    createFixOrientationMatrix(), true
                )
                scaledBitmap.recycle()
                output.use {
                    rotatedBitmap.compress(JPEG, 80, it)
                    rotatedBitmap.recycle()
                }
            }
        }
    }
}


fun Uri.createFixOrientationMatrix(): Matrix {
    val matrix = Matrix()
    catchAllError {
        val orientation = ExifInterface(application.openInputStream(this)!!)
            .getAttributeInt(TAG_ORIENTATION, ORIENTATION_NORMAL)
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
        if (orientation == ORIENTATION_UNDEFINED) fixRotationByContentQuery(matrix)
    }
    return matrix
}

fun Uri.fixRotationByContentQuery(matrix: Matrix) {
    val orientationColumn = arrayOf(ORIENTATION)
    val cur = application.contentResolver.query(this, orientationColumn, null, null, null)
    cur?.use {
        var orientation = -1
        if (cur.moveToFirst()) orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]))
        matrix.postRotate(orientation.toFloat())
    }
}