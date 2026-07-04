package renetik.android.event.change

/**
 * Controls how concurrent unsafe changes from multiple sources are delivered to a
 * combined callback (tuple [onUnsafeChange], [safeStateDelegate]).
 *
 * All modes update the internal cached values atomically; they differ only in how the
 * resulting callbacks are scheduled.
 */
enum class CSSafeChangeDelivery {
    /**
     * Latest-wins. Callbacks are serialized; intermediate snapshots may be coalesced into
     * the newest one, but the last delivered value is never stale. O(1) memory.
     * Correct default for derived state where only the current value matters.
     */
    Conflated,

    /**
     * Lossless FIFO. Every snapshot is delivered, in order, exactly once. Unbounded queue.
     * Use when each transition has a side effect that must not be dropped (event stream).
     */
    Buffered,

    /**
     * No serialization. The snapshot is taken under lock and fired immediately on the
     * writing thread. Lowest overhead, but concurrent emissions can be delivered out of
     * order (a stale snapshot can arrive last). Only for callbacks that recompute from
     * `.value` and tolerate the race.
     */
    Immediate,
}
