package renetik.android.widget

import android.widget.DatePicker
import renetik.android.java.util.calendar
import renetik.android.java.util.dateFrom
import java.util.*

val DatePicker.date: Date get() = calendar.dateFrom(year, month, dayOfMonth)