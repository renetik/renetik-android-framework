package renetik.android.java.util

import renetik.android.java.extensions.asInt
import java.util.*

val calendar: Calendar get() = Calendar.getInstance()

fun Calendar.from(date: Date) =
    apply { this.time = date }

fun Calendar.from(year: Int, month: Int? = null, day: Int? = null) =
    apply { set(year, month.asInt(), day.asInt()) }

fun Calendar.from(year: String?, month: String? = null, day: String? = null) =
    apply { from(year.asInt(), month.asInt(), day.asInt()) }

fun Calendar.dateFrom(year: Int, month: Int? = null, day: Int? = null): Date? {
    return from(year, month, day).time
}

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