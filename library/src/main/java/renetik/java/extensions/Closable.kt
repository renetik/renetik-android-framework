package renetik.java.extensions

import renetik.java.lang.tryAndError
import java.io.Closeable

fun close(closeable: Closeable?) = tryAndError { closeable?.close() }