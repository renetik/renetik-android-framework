package renetik.android.java.extensions

import renetik.android.framework.common.catchAllWarn

fun <T> Class<T>.createInstance() = catchAllWarn { this.newInstance() }