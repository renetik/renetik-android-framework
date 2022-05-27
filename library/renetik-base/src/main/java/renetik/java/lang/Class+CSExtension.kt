package renetik.java.lang

import renetik.android.framework.lang.catchAllWarn

fun <T> Class<T>.createInstance() = catchAllWarn { this.newInstance() }