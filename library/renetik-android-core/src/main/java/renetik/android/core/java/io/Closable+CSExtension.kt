package renetik.android.core.java.io

import renetik.android.core.lang.catchAllWarn
import java.io.Closeable

fun Closeable?.close() = catchAllWarn { this?.close() }