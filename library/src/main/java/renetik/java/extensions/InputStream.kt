package renetik.java.extensions

import renetik.java.lang.CSStringBuilderWriter
import renetik.java.lang.tryAndError
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.Writer

fun InputStream.copy(output: OutputStream): Int = copy(output, DEFAULT_BUFFER_SIZE).let { count ->
    if (count > Integer.MAX_VALUE) -1 else count.toInt()
}

fun InputStream.copy(output: OutputStream, bufferSize: Int = DEFAULT_BUFFER_SIZE) =
        copyTo(output, bufferSize)

fun InputStream.copy(output: Writer) = copy(InputStreamReader(this), output)

fun InputStream.asString() = tryAndError { copy(CSStringBuilderWriter()) }.stringify()
