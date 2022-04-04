package renetik.java.util

import android.content.Context
import android.text.format.DateFormat.getDateFormat
import android.text.format.DateFormat.getTimeFormat
import renetik.android.framework.common.catchAllWarn
import renetik.android.framework.common.catchError
import renetik.kotlin.notNull
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

val currentTime get() = Date().time
fun dateFromString(format: String, string: String) = catchError<ParseException> {
    SimpleDateFormat(format, Locale.US).parse("" + string)
}

fun Date.format(dateStyle: Int, timeStyle: Int): String =
    DateFormat.getDateTimeInstance(dateStyle, timeStyle).format(this)

fun Date.formatDate(style: Int): String = DateFormat.getDateInstance(style).format(this)
fun Date.formatTime(style: Int): String = DateFormat.getTimeInstance(style).format(this)
fun Date.format(format: String): String = SimpleDateFormat(format, Locale.US).format(this)
fun Date.addYears(value: Int): Date {
    val instance = Calendar.getInstance()
    instance.time = this
    instance.add(Calendar.YEAR, value)
    return instance.time
}

fun Date.addHours(value: Int): Date {
    val instance = Calendar.getInstance()
    instance.time = this
    instance.add(Calendar.HOUR, value)
    return instance.time
}


fun Date.createDatedDirName() = format("yyyy-MM-dd_HH-mm-ss")

fun Date.formatToISO8601(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US)
    dateFormat.timeZone = TimeZone.getTimeZone("CET")
    return dateFormat.format(this)
}

fun Context.parseTimeFormat(text: String) = catchAllWarn { getTimeFormat(this).parse(text) }
fun Context.parseDateFormat(text: String) = catchAllWarn { getDateFormat(this).parse(text) }
fun Context.formatDate(date: Date) = date.notNull { getDateFormat(this).format(date) }
fun Context.formatTime(date: Date) = date.notNull { getTimeFormat(this).format(date) }