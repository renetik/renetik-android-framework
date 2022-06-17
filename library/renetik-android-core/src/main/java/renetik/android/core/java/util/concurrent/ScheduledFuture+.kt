package renetik.android.core.java.util.concurrent

import java.util.concurrent.ScheduledFuture

fun ScheduledFuture<*>.cancel() = cancel(false)
fun ScheduledFuture<*>.cancel(interrupt: Boolean = false) = cancel(interrupt)