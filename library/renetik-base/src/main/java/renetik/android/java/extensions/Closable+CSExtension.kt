package renetik.android.java.extensions

import renetik.android.framework.common.catchAllWarn
import java.io.Closeable

fun close(closeable: Closeable?) = catchAllWarn { closeable?.close() }