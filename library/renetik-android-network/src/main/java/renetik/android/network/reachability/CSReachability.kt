package renetik.android.network.reachability

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import renetik.android.core.base.CSApplication.Companion.app
import renetik.android.core.extensions.content.asString
import renetik.android.core.extensions.content.isNetworkConnected
import renetik.android.core.extensions.content.register
import renetik.android.core.extensions.content.unregister
import renetik.android.core.logging.CSLog.logDebug
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.CSHasDestruct
import renetik.android.event.common.CSModel

class CSReachability(parent: CSHasDestruct) : CSModel(parent) {
    val eventOnConnected = event<CSReachability>()
    val eventOnDisConnected = event<CSReachability>()
    val eventOnStateChanged = event<CSReachability>()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            logDebug { intent.asString }
            onNetworkStateChange()
        }
    }
    var started: Boolean = false

    fun start(): CSReachability {
        if (!started) {
            @Suppress("DEPRECATION")
            app.register(receiver, IntentFilter(CONNECTIVITY_ACTION))
            started = true
        }
        return this
    }

    fun stop(): CSReachability {
        if (started) {
            started = false
            app.unregister(receiver)
        }
        return this
    }

    private fun onNetworkStateChange() {
        if (app.isNetworkConnected) onNetworkConnected()
        else onNetworkDisconnected()
        eventOnStateChanged.fire(this)
    }

    protected fun onNetworkConnected() {
        eventOnConnected.fire(this)
    }

    protected fun onNetworkDisconnected() {
        eventOnDisConnected.fire(this)
    }

    override fun onDestruct() {
        super.onDestruct()
        stop()
    }
}
