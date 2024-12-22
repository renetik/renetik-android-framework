package renetik.android.controller.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_HEADSET_PLUG
import android.content.IntentFilter
import android.view.View
import renetik.android.controller.base.CSActivityView
import renetik.android.core.extensions.content.register
import renetik.android.core.extensions.content.unregister
import renetik.android.core.logging.CSLog.logInfo
import renetik.android.core.logging.CSLog.logWarn

@Deprecated("")
class CSHeadsetAudioPlugDetector(
    parent: CSActivityView<*>, val onHeadsetPlugChanged: (isPlugged: Boolean) -> Unit)
    : CSActivityView<View>(parent) {

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) = onReceive(intent)
    }

    override fun onResume() {
        super.onResume()
        register(receiver, IntentFilter(ACTION_HEADSET_PLUG))
    }

    override fun onPause() {
        super.onPause()
        unregister(receiver)
    }

    fun onReceive(intent: Intent) {
        if (intent.action == ACTION_HEADSET_PLUG) {
            logInfo {
                "ACTION_HEADSET_PLUG isInitialStickyBroadcast " +
                        "${receiver.isInitialStickyBroadcast}"
            }
            when (intent.getIntExtra("state", -1)) {
                0 -> {
                    logInfo { "ACTION_HEADSET_PLUG isUnplugged " }
                    onHeadsetPlugChanged(false)
                }
                1 -> {
                    logInfo { "ACTION_HEADSET_PLUG isPlugged " }
                    onHeadsetPlugChanged(true)
                }
                else -> logWarn { "ACTION_HEADSET_PLUG unknown " }
            }
        }
    }
}