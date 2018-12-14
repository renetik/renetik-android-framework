package renetik.android.extensions

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.util.DisplayMetrics
import androidx.core.content.ContextCompat
import renetik.java.collections.CSList
import renetik.java.collections.list
import renetik.java.lang.tryAndCatch
import renetik.java.lang.tryAndFinally
import renetik.java.lang.tryAndWarn
import java.io.ByteArrayOutputStream

fun Context.color(color: Int): Int? =
        tryAndCatch(NotFoundException::class, { resourceColor(color) }, { color })

fun Context.resourceColor(color: Int) = ContextCompat.getColor(this, color)

fun Context.resourceString(id: Int) = resources.getString(id)

fun Context.resourceBytes(id: Int) = tryAndWarn {
    val stream = resources.openRawResource(id)
    val outputStream = ByteArrayOutputStream()
    val buffer = ByteArray(4 * 1024)
    tryAndFinally({
        var read: Int
        do {
            read = stream.read(buffer, 0, buffer.size)
            if (read == -1) break
            outputStream.write(buffer, 0, read)
        } while (true)
        outputStream.toByteArray()
    }) { stream.close() }
}

fun Context.resourceDimension(id: Int) = resources.getDimension(id).toInt()

fun Context.resourceStrings(id: Int): CSList<String>? =
        tryAndWarn(NotFoundException::class) { list(*resources.getStringArray(id)) }

fun Context.resourceInts(id: Int) =
        tryAndWarn(NotFoundException::class) { list(resources.getIntArray(id).asList()) }

val Context.displayMetrics get():DisplayMetrics = resources.displayMetrics
fun Context.toDp(pixel: Int) = pixel / (displayMetrics.densityDpi / 160f)
fun Context.toPixel(dp: Float) = (dp * (displayMetrics.densityDpi / 160f)).toInt()
fun Context.toPixel(dp: Int) = toPixel(dp.toFloat())

fun Context.colorFromAttribute(attribute: Int): Int {
    val attributes = obtainStyledAttributes(intArrayOf(attribute))
    val color = attributes.getColor(0, 0)
    attributes.recycle()
    return color
}

fun Context.dimensionFromAttribute(attribute: Int): Int {
    val attributes = obtainStyledAttributes(intArrayOf(attribute))
    val dimension = attributes.getDimensionPixelSize(0, 0)
    attributes.recycle()
    return dimension
}

fun Context.resourceFromAttribute(attribute: Int): Int {
    val attributes = obtainStyledAttributes(intArrayOf(attribute))
    val resource = attributes.getResourceId(0, 0)
    attributes.recycle()
    return resource
}