package renetik.android.framework

import renetik.android.framework.event.*

open class CSModelBase() : CSEventOwnerHasDestroy {

    constructor(context: CSHasDestroy) : this() {
        eventRegistrations.add(context.eventDestroy.listenOnce { onDestroy() })
    }

    private var isDestroyed = false

    override val eventDestroy = event<Unit>()

    override fun onDestroy() {
        eventRegistrations.cancel()
        // _isDestroyed should be set before event so we now we should not
        // destroy again in some actions as remove from superview
        isDestroyed = true
        eventDestroy.fire().clear()
    }

    override val eventRegistrations = CSEventRegistrations()
}

