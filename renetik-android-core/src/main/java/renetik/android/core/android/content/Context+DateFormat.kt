package renetik.android.core.android.content

import android.content.Context
import android.text.format.DateFormat.getDateFormat
import android.text.format.DateFormat.getTimeFormat
import renetik.android.core.lang.catchAllWarn
import java.util.Date

fun Context.parseTimeFormat(text: String) = catchAllWarn { getTimeFormat(this).parse(text) }
fun Context.parseDateFormat(text: String) = catchAllWarn { getDateFormat(this).parse(text) }
fun Context.formatDate(date: Date): String = getDateFormat(this).format(date)
fun Context.formatTime(date: Date): String = getTimeFormat(this).format(date)
