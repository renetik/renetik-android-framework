package renetik.android.java.extensions

import renetik.android.java.common.catchAllWarn

fun <T> Class<T>.createInstance() = catchAllWarn { this.newInstance() }