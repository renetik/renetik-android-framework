package renetik.java.util

import renetik.kotlin.asInt
import java.util.*
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.MINUTE

val calendar: Calendar get() = Calendar.getInstance()

fun Calendar.fromTime(time: Long) =
    apply { this.timeInMillis = time }

fun Calendar.from(date: Date) =
    apply { this.time = date }

fun Calendar.from(year: Int, month: Int? = null, day: Int? = null) =
    apply { set(year, month.asInt, day.asInt, 0, 0, 0) }

fun Calendar.from(year: String?, month: String? = null, day: String? = null) =
    apply { from(year.asInt, month.asInt, day.asInt) }

fun Calendar.dateFrom(year: Int, month: Int? = null, day: Int? = null) =
    from(year, month, day).time

fun Calendar.timeFrom(hour: Int, minute: Int) = apply {
    set(HOUR_OF_DAY, hour); set(MINUTE, minute)
}.time

fun Calendar.dateFrom(year: String?, month: String? = null, day: String? = null): Date? {
    if (year == null) return null
    return dateFrom(year.toInt(), month?.toInt(), day?.toInt())
}

val Calendar.age: Int
    get() {
        val today = calendar
        var age = today[Calendar.YEAR] - this[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < this[Calendar.DAY_OF_YEAR]) age--
        return age
    }