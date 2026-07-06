package renetik.android.event.change

import renetik.android.core.lang.synchronized
import renetik.android.event.registration.CSRegistration

/**
 * Latest-wins emitter ([CSSafeChangeDelivery.Conflated]). Callbacks are serialized through a
 * single pending slot: while a callback is running, newer snapshots overwrite the pending one,
 * so intermediate snapshots may be coalesced. The last delivered value is never stale and
 * memory stays O(1).
 */
internal class CSConflatingSafeChangeEmitter<Change>(
    private val registration: CSRegistration,
    private val onChange: (Change) -> Unit,
) : CSSafeChangeEmitter<Change> {
    private class Emission<Change>(val value: Change)

    private val lock = Any()
    private var pending: Emission<Change>? = null
    private var isDraining = false

    override fun update(function: () -> Unit) = synchronized<Any, Unit>(lock) {
        function()
    }

    override fun enqueue(produce: () -> Change) {
        var shouldDrain = false
        synchronized<Any, Unit>(lock) {
            pending = Emission(produce())
            if (!isDraining) {
                isDraining = true
                shouldDrain = true
            }
        }
        if (shouldDrain) drain()
    }

    private fun drain() {
        var completed = false
        try {
            while (true) {
                val emission = synchronized<Any, Emission<Change>?>(lock) {
                    val next = if (registration.isActive) pending else null
                    pending = null
                    if (next == null) isDraining = false
                    next
                } ?: run {
                    completed = true
                    return
                }
                if (registration.isActive) onChange(emission.value)
            }
        } finally {
            if (!completed) synchronized<Any, Unit>(lock) {
                isDraining = false
            }
        }
    }
}
