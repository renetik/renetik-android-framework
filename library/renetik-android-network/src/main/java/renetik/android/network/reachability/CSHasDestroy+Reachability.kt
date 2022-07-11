package renetik.android.network.reachability

import renetik.android.event.listenOnce
import renetik.android.event.common.CSHasDestroy

fun CSHasDestroy.onInternetConnected(function: () -> Unit): CSReachability {
    val reachability = CSReachability().start()
    reachability.eventOnConnected.listenOnce { function() }
    eventDestroy.listen { reachability.stop() }
    return reachability
}