package renetik.android.core.java.lang

import renetik.android.core.lang.catchAllWarn

fun <T> Class<T>.createInstance() = catchAllWarn { this.newInstance() }