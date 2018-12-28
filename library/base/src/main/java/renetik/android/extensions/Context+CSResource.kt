package renetik.android.extensions

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.core.content.ContextCompat
import renetik.android.base.application
import renetik.android.java.collections.CSList
import renetik.android.java.collections.list
import renetik.android.java.common.tryAndCatch
import renetik.android.java.common.tryAndFinally
import renetik.android.java.common.tryAndWarn
import renetik.android.java.extensions.isEmpty
import renetik.android.java.extensions.stringify
import java.io.ByteArrayOutputStream

fun Context.color(value: Int): Int {
    return tryAndCatch(NotFoundException::class, { application.resourceColor(value) }, {
        tryAndCatch(NotFoundException::class, { colorFromAttribute(value) }, { value })
    })
}

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


private fun Context.resolveAttribute(attribute: Int) =
        TypedValue().apply { theme.resolveAttribute(attribute, this, true) }

fun Context.colorFromAttribute(attribute: Int): Int {
    val color = resolveAttribute(attribute).data
    if (color.isEmpty) throw NotFoundException()
    return color
}


fun Context.dimensionFromAttribute(attribute: Int): Int {
    val attributes = obtainStyledAttributes(intArrayOf(attribute))
    val dimension = attributes.getDimensionPixelSize(0, 0)
    attributes.recycle()
    return dimension
}

fun Context.stringFromAttribute(attribute: Int) = resolveAttribute(attribute).string.stringify()
fun Context.stringFromAttribute2(attribute: Int) = stringFromAttribute(intArrayOf(attribute), 0)

fun Context.stringFromAttribute(styleable: IntArray, styleableAttribute: Int): String {
    val attributes = obtainStyledAttributes(styleable)
    val string = attributes.getString(styleableAttribute)
    attributes.recycle()
    return string.stringify()
}

fun Context.resourceFromAttribute(attribute: Int): Int {
    val resourceId = resolveAttribute(attribute).resourceId
    if (resourceId.isEmpty) throw NotFoundException()
    return resourceId
}