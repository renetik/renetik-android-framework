package renetik.android.core.java.util.concurrent

import java.util.concurrent.ScheduledFuture

fun ScheduledFuture<*>.cancel() = cancel(interrupt = false)

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
fun ScheduledFuture<*>.cancel(interrupt: Boolean) = cancel(interrupt)