package renetik.java.extensions

import java.io.Reader
import java.io.Writer

fun copy(input: Reader, output: Writer, bufferSize: Int = DEFAULT_BUFFER_SIZE): Long {
    var bytesCopied: Long = 0
    val buffer = CharArray(bufferSize)
    var bytes = input.read(buffer)
    while (bytes >= 0) {
        output.write(buffer, 0, bytes)
        bytesCopied += bytes
        bytes = input.read(buffer)
    }
    return bytesCopied
}

fun copy(input: Reader, output: Writer): Int {
    val count = copy(input, output, DEFAULT_BUFFER_SIZE)
    return if (count > Integer.MAX_VALUE) -1 else count.toInt()
}