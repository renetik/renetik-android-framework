package renetik.android.java.util

import renetik.android.java.extensions.asInt
import java.util.*

val calendar: Calendar get() = Calendar.getInstance()

fun Calendar.dateFrom(year: Int?, month: Int? = null, day: Int? = null): Date? {
    if (year == null) return null
    val calendar = Calendar.getInstance()
    calendar.set(year.asInt(), month.asInt(), day.asInt())
    return calendar.time
}

fun Calendar.dateFrom(year: String?, month: String? = null, day: String? = null) =
    calendar.dateFrom(year?.toInt(), month?.toInt(), day?.toInt())