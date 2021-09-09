package renetik.android.widget

import android.widget.TimePicker
import renetik.android.java.util.calendar
import renetik.android.java.util.timeFrom
import java.util.*

@Suppress("DEPRECATION")
val TimePicker.time: Date
    get() = calendar.timeFrom(currentHour, currentMinute)