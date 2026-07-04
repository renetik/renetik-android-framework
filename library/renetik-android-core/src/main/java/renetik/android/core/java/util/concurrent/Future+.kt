package renetik.android.core.java.util.concurrent

import java.util.concurrent.Future

inline val <V> Future<V>?.isNullOrActive get() = this == null || !isDone

fun Future<*>.cancelNotInterrupt() = cancel(false)

fun Future<*>.cancelInterrupt() = cancel(true)