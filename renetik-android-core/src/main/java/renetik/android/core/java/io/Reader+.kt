package renetik.android.core.java.io

import java.io.Reader
import java.io.Writer

fun Reader.copy(output: Writer, bufferSize: Int = DEFAULT_BUFFER_SIZE): Long {
    var bytesCopied: Long = 0
    val buffer = CharArray(bufferSize)
    var bytes = read(buffer)
    while (bytes >= 0) {
        output.write(buffer, 0, bytes)
        bytesCopied += bytes
        bytes = read(buffer)
    }
    return bytesCopied
}

fun Reader.copy(output: Writer): Int {
    val count = copy(output, DEFAULT_BUFFER_SIZE)
    return if (count > Integer.MAX_VALUE) -1 else count.toInt()
}