package renetik.android.java.extensions

import renetik.android.java.common.CSStringBuilderWriter
import renetik.android.java.common.tryAndError
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.Writer

fun InputStream.copy(output: OutputStream, bufferSize: Int = DEFAULT_BUFFER_SIZE) =
        copyTo(output, bufferSize)

fun InputStream.copy(output: Writer) = copy(InputStreamReader(this), output)

fun InputStream.asString() = CSStringBuilderWriter().apply {
    tryAndError { copy(this) }
}.stringify()
