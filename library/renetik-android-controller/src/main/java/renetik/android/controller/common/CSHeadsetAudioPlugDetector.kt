package renetik.android.controller.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_HEADSET_PLUG
import android.content.IntentFilter
import android.view.View
import renetik.android.controller.base.CSActivityView
import renetik.android.core.logging.CSLog.logInfo
import renetik.android.core.logging.CSLog.logWarn
import renetik.android.core.logging.CSLogMessage.Companion.message

class CSHeadsetAudioPlugDetector(
    parent: CSActivityView<*>, val onHeadsetPlugChanged: (isPlugged: Boolean) -> Unit)
    : CSActivityView<View>(parent) {

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) = onReceive(intent)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, IntentFilter(ACTION_HEADSET_PLUG))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    fun onReceive(intent: Intent) {
        if (intent.action == ACTION_HEADSET_PLUG) {
            logInfo {
                message("ACTION_HEADSET_PLUG isInitialStickyBroadcast " +
                        "${receiver.isInitialStickyBroadcast}")
            }
            when (intent.getIntExtra("state", -1)) {
                0 -> {
                    logInfo { message("ACTION_HEADSET_PLUG isUnplugged ") }
                    onHeadsetPlugChanged(false)
                }
                1 -> {
                    logInfo { message("ACTION_HEADSET_PLUG isPlugged ") }
                    onHeadsetPlugChanged(true)
                }
                else -> logWarn { message("ACTION_HEADSET_PLUG unknown ") }
            }
        }
    }
}