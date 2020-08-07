package renetik.android.extensions

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources.NotFoundException
import android.net.Uri
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import renetik.android.base.CSApplicationObject.application
import renetik.android.java.common.tryAndCatch
import renetik.android.java.common.tryAndError
import renetik.android.java.common.tryAndFinally
import renetik.android.java.common.tryAndWarn
import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.isEmpty
import renetik.android.java.extensions.stringify
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream

@ColorInt
fun Context.color(value: Int): Int {
    return tryAndCatch(NotFoundException::class, { application.resourceColor(value) }, {
        tryAndCatch(NotFoundException::class, { colorFromAttribute(value) }, { value })
    })
}

@ColorInt
fun Context.resourceColor(@ColorRes color: Int) = ContextCompat.getColor(this, color)

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

fun Context.openInputStream(uri: Uri) = tryAndError(FileNotFoundException::class) {
    contentResolver.openInputStream(uri)
}

fun Context.resourceDimension(id: Int) = resources.getDimension(id).toInt()

fun Context.resourceStrings(id: Int): List<String>? =
    tryAndWarn(NotFoundException::class) { list(*resources.getStringArray(id)) }

fun Context.resourceInts(id: Int) =
    tryAndWarn(NotFoundException::class) { list(resources.getIntArray(id).asList()) }

val Context.displayMetrics get():DisplayMetrics = resources.displayMetrics

fun Context.toDpF(pixel: Int) = pixel / (displayMetrics.densityDpi / 160f)
fun Context.toDp(pixel: Int) = toDpF(pixel).toInt()

fun Context.toPixelF(dp: Float) = (dp * (displayMetrics.densityDpi / 160f))
fun Context.toPixelF(dp: Int) = toPixelF(dp.toFloat())
fun Context.toPixel(dp: Float) = toPixelF(dp).toInt()
fun Context.toPixel(dp: Int) = toPixelF(dp.toFloat()).toInt()

private fun Context.resolveAttribute(attribute: Int) =
    TypedValue().apply { theme.resolveAttribute(attribute, this, true) }

fun Context.colorFromAttribute(attribute: Int) = resolveAttribute(attribute).data.apply {
    if (isEmpty) throw NotFoundException()
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

fun Context.resourceFromAttribute(attribute: Int) = resolveAttribute(attribute).resourceId.apply {
    if (isEmpty) throw NotFoundException()
}

fun Context.openAsset(path: String): InputStream {
    return assets.open(path)
}

fun Context.readAsset(path: String): String {
    return openAsset(path).bufferedReader().use { it.readText() }
}

val Context.isPortrait get() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
val Context.isLandscape get() = !isPortrait

