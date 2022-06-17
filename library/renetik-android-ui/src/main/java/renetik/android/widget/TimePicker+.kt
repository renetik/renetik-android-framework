package renetik.android.widget

import android.widget.TimePicker
import renetik.android.core.java.util.calendar
import renetik.android.core.java.util.timeFrom
import java.util.*

@Suppress("DEPRECATION")
val TimePicker.time: Date
    get() = calendar.timeFrom(currentHour, currentMinute)