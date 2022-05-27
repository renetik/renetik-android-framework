package renetik.java.io

import renetik.android.framework.lang.catchAllWarn
import java.io.Closeable

fun Closeable?.close() = catchAllWarn { this?.close() }