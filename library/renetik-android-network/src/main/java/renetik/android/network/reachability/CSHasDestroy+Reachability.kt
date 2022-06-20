package renetik.android.network.reachability

import renetik.android.event.listen
import renetik.android.event.listenOnce
import renetik.android.event.owner.CSHasDestroy

fun CSHasDestroy.onInternetConnected(function: () -> Unit): CSReachability {
    val reachability = CSReachability().start()
    reachability.eventOnConnected.listenOnce { function() }
    eventDestroy.listen { reachability.stop() }
    return reachability
}