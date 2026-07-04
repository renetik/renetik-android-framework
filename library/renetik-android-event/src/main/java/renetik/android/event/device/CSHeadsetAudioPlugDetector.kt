package renetik.android.event.device

import android.content.BroadcastReceiver
import android.content.Intent.ACTION_HEADSET_PLUG
import renetik.android.core.android.content.register
import renetik.android.core.android.content.unregister
import renetik.android.core.base.CSApplication.Companion.app
import renetik.android.core.logging.CSLog.logDebug
import renetik.android.core.logging.CSLog.logWarn
import renetik.android.event.lifecycle.CSModel

class CSHeadsetAudioPlugDetector(
    parent: CSModel,
    val onHeadsetPlugChanged: (isPlugged: Boolean) -> Unit
) : CSModel(parent) {

    private var isPlugged: Boolean? = null

    private val receiver = app.register(ACTION_HEADSET_PLUG) { intent, receiver ->
        if (intent.action == ACTION_HEADSET_PLUG)
            when (val state = intent.getIntExtra("state", -1)) {
                0, 1 -> onStateReceived(state, receiver)
                else -> logWarn { "ACTION_HEADSET_PLUG unknown " }
            }
    }

    private fun onStateReceived(state: Int, receiver: BroadcastReceiver) {
        val isPlugged = state != 0
        if (this.isPlugged != isPlugged) {
            this.isPlugged = isPlugged
            logDebug { "ACTION_HEADSET_PLUG isUnplugged:$isPlugged" }
            if (!receiver.isInitialStickyBroadcast) onHeadsetPlugChanged(isPlugged)
        }
    }

    override fun onDestruct() {
        app.unregister(receiver)
        super.onDestruct()
    }
}