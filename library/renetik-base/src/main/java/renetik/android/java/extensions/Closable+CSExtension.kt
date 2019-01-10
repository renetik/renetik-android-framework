package renetik.android.java.extensions

import renetik.android.java.common.tryAndError
import java.io.Closeable

fun close(closeable: Closeable?) = tryAndError { closeable?.close() }