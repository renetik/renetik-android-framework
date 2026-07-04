package renetik.android.event.change

import renetik.android.core.lang.synchronized
import renetik.android.event.registration.CSRegistration

/**
 * Lossless FIFO emitter ([CSSafeChangeDelivery.Buffered]). Every snapshot is delivered, in
 * enqueue order, exactly once. A single drainer thread runs callbacks outside the lock while
 * other producers append and return. Queue is unbounded.
 */
internal class CSBufferedSafeChangeEmitter<Change>(
    private val registration: CSRegistration,
    private val onChange: (Change) -> Unit,
) : CSSafeChangeEmitter<Change> {
    private class Emission<Change>(val value: Change)

    private val lock = Any()
    private val changes = ArrayDeque<Change>()
    private var isDraining = false

    override fun update(function: () -> Unit) = synchronized<Any, Unit>(lock) {
        function()
    }

    override fun enqueue(produce: () -> Change) {
        var shouldDrain = false
        synchronized<Any, Unit>(lock) {
            changes.addLast(produce())
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
                    if (!registration.isActive) {
                        changes.clear()
                        isDraining = false
                        null
                    } else if (changes.isEmpty()) {
                        isDraining = false
                        null
                    } else Emission(changes.removeFirst())
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
