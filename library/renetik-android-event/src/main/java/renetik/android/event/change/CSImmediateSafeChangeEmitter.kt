package renetik.android.event.change

import renetik.android.core.lang.synchronized
import renetik.android.event.registration.CSRegistration

/**
 * No-serialization emitter ([CSSafeChangeDelivery.Immediate]). The snapshot is captured under
 * lock and fired immediately on the writing thread. Lowest overhead, but concurrent emissions
 * can be delivered out of order — a stale snapshot can arrive last. Only safe for callbacks
 * that recompute from `.value` and tolerate the race.
 */
internal class CSImmediateSafeChangeEmitter<Change>(
    private val registration: CSRegistration,
    private val onChange: (Change) -> Unit,
) : CSSafeChangeEmitter<Change> {
    private val lock = Any()

    override fun update(function: () -> Unit) = synchronized<Any, Unit>(lock) {
        function()
    }

    override fun enqueue(produce: () -> Change) {
        val change = synchronized<Any, Change>(lock) { produce() }
        if (registration.isActive) onChange(change)
    }
}
