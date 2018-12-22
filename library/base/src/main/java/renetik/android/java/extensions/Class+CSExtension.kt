package renetik.android.java.extensions

import renetik.android.java.common.tryAndWarn

fun <T> Class<T>.createInstance() = tryAndWarn { this.newInstance() }