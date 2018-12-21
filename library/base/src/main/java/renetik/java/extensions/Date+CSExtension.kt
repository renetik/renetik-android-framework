package renetik.java.extensions

import renetik.java.lang.tryAndError
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun currentTime() = Date().time
fun dateFromString(format: String, string: String) =
        tryAndError(ParseException::class) { SimpleDateFormat(format, Locale.US).parse("" + string) }

fun Date.format(dateStyle: Int, timeStyle: Int): String = DateFormat.getDateTimeInstance(dateStyle, timeStyle).format(this)
fun Date.formatDate(style: Int): String = DateFormat.getDateInstance(style).format(this)
fun Date.formatTime(style: Int): String = DateFormat.getTimeInstance(style).format(this)
fun Date.format(format: String): String = SimpleDateFormat(format, Locale.US).format(this)
fun Date.addYears(value: Int): Date {
    val instance = Calendar.getInstance()
    instance.time = this
    instance.add(Calendar.YEAR, value)
    return instance.time
}

fun Date.createDatedDirName() = format("yyyy-MM-dd_HH-mm-ss")