package renetik.android.java.extensions

import renetik.android.java.common.catchAllWarn
import java.io.Closeable

fun close(closeable: Closeable?) = catchAllWarn { closeable?.close() }