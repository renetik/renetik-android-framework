package renetik.android.event.owner

import renetik.android.event.registration.CSRegistrations
import renetik.android.event.register
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.fire
import renetik.android.event.listenOnce

open class CSEventOwnerHasDestroyBase(parent: CSHasDestroy? = null) : CSEventOwnerHasDestroy {

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

