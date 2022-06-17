package renetik.android.core.java.io

import renetik.android.core.common.CSStringBuilderWriter
import renetik.android.core.lang.catchAllError
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.Writer

fun InputStream.copy(output: OutputStream, bufferSize: Int = DEFAULT_BUFFER_SIZE) =
    copyTo(output, bufferSize)

fun InputStream.copy(output: Writer) = InputStreamReader(this).copy(output)

fun InputStream.readText(): String = CSStringBuilderWriter().apply {
    catchAllError { copy(this) }
}.toString()