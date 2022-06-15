package renetik.android.framework.base

import renetik.android.framework.event.*
import renetik.android.framework.event.CSEvent.Companion.event
import renetik.android.framework.protocol.CSEventOwnerHasDestroy
import renetik.android.framework.protocol.CSHasDestroy

open class CSBase(parent: CSHasDestroy? = null) : CSEventOwnerHasDestroy {

    final override val registrations = CSRegistrations()
    override val eventDestroy = event<Unit>()
    var isDestroyed = false
        private set

    init {
        parent?.let { register(it.eventDestroy.listenOnce { onDestroy() }) }
    }

    override fun onDestroy() {
        registrations.cancel()
        isDestroyed = true
        eventDestroy.fire().clear()
    }

//    override fun toString() = "${super.toString()}"
}

