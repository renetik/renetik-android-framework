package renetik.java.io

import renetik.android.framework.common.catchAllWarn
import java.io.Closeable

fun Closeable?.close() = catchAllWarn { this?.close() }