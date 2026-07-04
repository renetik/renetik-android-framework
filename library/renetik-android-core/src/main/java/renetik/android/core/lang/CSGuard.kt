package renetik.android.core.lang

object CSGuard {
    inline fun <T1, R> guard(
        p1: T1?,
        condition: Boolean = true,
        block: (T1) -> R
    ): R? = if (p1 != null && condition)
        block(p1) else null

    inline fun <T1, T2, R> guard(
        p1: T1?, p2: T2?,
        block: (Pair<T1, T2>) -> R
    ): R? = guard(p1, p2, true, block)

    inline fun <T1, T2, R> guard(
        p1: T1?, p2: T2?,
        condition: Boolean = true,
        block: (Pair<T1, T2>) -> R
    ): R? = if (p1 != null && p2 != null && condition)
        block(p1 to p2) else null

    inline fun <T1, T2, T3, R> guard(
        p1: T1?, p2: T2?, p3: T3?,
        block: (T1, T2, T3) -> R
    ): R? = guard(p1, p2, p3, true, block)

    inline fun <T1, T2, T3, R> guard(
        p1: T1?, p2: T2?, p3: T3?,
        condition: Boolean = true,
        block: (T1, T2, T3) -> R
    ): R? = if (p1 != null && p2 != null && p3 != null && condition)
        block(p1, p2, p3) else null

    inline fun <T1, T2, T3, T4, R> guard(
        p1: T1?, p2: T2?, p3: T3?, p4: T4?,
        condition: Boolean = true,
        block: (T1, T2, T3, T4) -> R
    ): R? = if (p1 != null && p2 != null && p3 != null && p4 != null && condition)
        block(p1, p2, p3, p4) else null
}