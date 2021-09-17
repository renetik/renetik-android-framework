package renetik.android.controller.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.View
import renetik.android.controller.base.CSActivityView
import renetik.android.framework.logging.CSLog.info
import renetik.android.framework.logging.CSLog.warn

class CSHeadsetAudioPlugDetector(
    parent: CSActivityView<*>, val onHeadsetPlugChanged: (isPlugged: Boolean) -> Unit)
    : CSActivityView<View>(parent) {

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) = onReceive(intent)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, IntentFilter(Intent.ACTION_HEADSET_PLUG))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    fun onReceive(intent: Intent) {
        if (intent.action == Intent.ACTION_HEADSET_PLUG) {
            info("ACTION_HEADSET_PLUG isInitialStickyBroadcast",
                    receiver.isInitialStickyBroadcast)
            when (intent.getIntExtra("state", -1)) {
                0 -> {
                    info("ACTION_HEADSET_PLUG isUnplugged")
                    onHeadsetPlugChanged(false)
                }
                1 -> {
                    info("ACTION_HEADSET_PLUG isPlugged")
                    onHeadsetPlugChanged(true)
                }
                else -> warn("ACTION_HEADSET_PLUG unknown")
            }
        }
    }
}