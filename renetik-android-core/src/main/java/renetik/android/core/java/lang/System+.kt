package renetik.android.core.java.lang

import java.lang.System.currentTimeMillis
import kotlin.time.Duration
import kotlin.time.DurationUnit.MILLISECONDS
import kotlin.time.toDuration

val nowDuration: Duration get() = currentTimeMillis().toDuration(MILLISECONDS)