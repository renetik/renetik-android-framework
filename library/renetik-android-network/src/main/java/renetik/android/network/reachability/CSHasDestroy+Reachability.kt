package renetik.android.network.reachability

import renetik.android.event.common.CSHasDestroy
import renetik.android.event.listenOnce

//fun CSHasDestroy.onInternetConnected(function: () -> Unit): CSReachability {
//    val reachability = CSReachability().start()
//    reachability.eventOnConnected.listenOnce { function() }
//    eventDestroy.listenOnce { reachability.stop() }
//    return reachability
//}