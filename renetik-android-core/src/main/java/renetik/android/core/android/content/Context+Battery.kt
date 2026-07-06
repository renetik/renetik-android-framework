package renetik.android.core.android.content

import android.content.Context
import android.content.Intent.ACTION_BATTERY_CHANGED
import android.content.IntentFilter
import android.os.BatteryManager.EXTRA_LEVEL
import android.os.BatteryManager.EXTRA_SCALE

val Context.batteryPercent: Float
    get() {
        val batteryStatus = register(IntentFilter(ACTION_BATTERY_CHANGED))
        val level = batteryStatus!!.getIntExtra(EXTRA_LEVEL, -1)
        val scale = batteryStatus.getIntExtra(EXTRA_SCALE, -1)
        return level / scale.toFloat()
    }
