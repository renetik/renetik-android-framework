package renetik.android.event.change

import renetik.android.event.registration.CSRegistration

/**
 * Serializes combined-change snapshots produced under a shared lock and dispatches them to
 * [onChange] outside the lock. Implementations differ in their drop/order guarantees, see
 * [CSSafeChangeDelivery].
 *
 * Both [update] and [enqueue] run their [function]/[produce] block under the emitter lock, so
 * the cached values they mutate stay consistent with the snapshot that is captured.
 */
internal interface CSSafeChangeEmitter<Change> {
    /** Runs [function] under the lock without scheduling a callback (used for initial resync). */
    fun update(function: () -> Unit)

    /** Runs [produce] under the lock to capture a snapshot, then schedules its delivery. */
    fun enqueue(produce: () -> Change)
}

internal fun <Change> CSSafeChangeDelivery.emitter(
    registration: CSRegistration,
    onChange: (Change) -> Unit,
): CSSafeChangeEmitter<Change> = when (this) {
    CSSafeChangeDelivery.Conflated -> CSConflatingSafeChangeEmitter(registration, onChange)
    CSSafeChangeDelivery.Buffered -> CSBufferedSafeChangeEmitter(registration, onChange)
    CSSafeChangeDelivery.Immediate -> CSImmediateSafeChangeEmitter(registration, onChange)
}
