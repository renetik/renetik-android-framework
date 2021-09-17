package renetik.java.util.concurrent

import java.util.concurrent.ScheduledFuture

fun ScheduledFuture<*>.cancel() = cancel(false)