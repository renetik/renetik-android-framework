package renetik.android.framework

import renetik.android.framework.event.*

open class CSModelBase(parent: CSHasDestroy? = null) : CSEventOwnerHasDestroy {
    override val eventRegistrations = CSEventRegistrations()
    private var isDestroyed = false
    override val eventDestroy = event<Unit>()

    init {
        parent?.let { eventRegistrations.add(it.eventDestroy.listenOnce { onDestroy() }) }
    }

    override fun onDestroy() {
        eventRegistrations.cancel()
        // _isDestroyed should be set before event so we now we should not
        // destroy again in some actions as remove from superview
        isDestroyed = true
        eventDestroy.fire().clear()
    }

}

