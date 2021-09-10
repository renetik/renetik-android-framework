package renetik.android.widget

import android.widget.TimePicker
import renetik.java.util.calendar
import renetik.java.util.timeFrom
import java.util.*

@Suppress("DEPRECATION")
val TimePicker.time: Date
    get() = calendar.timeFrom(currentHour, currentMinute)