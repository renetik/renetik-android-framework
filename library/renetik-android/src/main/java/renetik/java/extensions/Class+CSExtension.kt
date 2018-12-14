package renetik.java.extensions

import renetik.java.lang.tryAndWarn

fun <T> Class<T>.createInstance() = tryAndWarn { this.newInstance() }