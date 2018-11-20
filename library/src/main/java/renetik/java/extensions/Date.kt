package renetik.java.extensions

import renetik.java.lang.tryAndError
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun currentTime() = Date().time
fun dateFromString(format: String, string: String) =
        tryAndError(ParseException::class) { SimpleDateFormat(format).parse("" + string) }

fun Date.format(dateStyle: Int, timeStyle: Int): String = DateFormat.getDateTimeInstance(dateStyle, timeStyle).format(this)
fun Date.formatDate(style: Int): String = DateFormat.getDateInstance(style).format(this)
fun Date.formatTime(style: Int): String = DateFormat.getTimeInstance(style).format(this)
fun Date.stringFromDate(format: String) = SimpleDateFormat(format).format(this)
fun Date.addYears(value: Int): Date {
    val instance = Calendar.getInstance()
    instance.time = this
    instance.add(Calendar.YEAR, value)
    return instance.time
}