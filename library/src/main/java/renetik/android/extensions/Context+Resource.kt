package renetik.android.extensions

import android.content.Context
import android.content.res.Resources
import androidx.core.content.ContextCompat
import renetik.java.collections.CSList
import renetik.java.collections.list
import renetik.java.lang.tryAndFinally
import renetik.java.lang.tryAndWarn
import java.io.ByteArrayOutputStream

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
        tryAndWarn(Resources.NotFoundException::class) { list(*resources.getStringArray(id)) }

fun Context.resourceInts(id: Int) =
        tryAndWarn(Resources.NotFoundException::class) { list(resources.getIntArray(id).asList()) }