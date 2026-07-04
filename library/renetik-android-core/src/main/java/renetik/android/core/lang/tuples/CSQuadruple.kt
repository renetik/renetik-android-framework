package renetik.android.core.lang.tuples

import java.io.Serializable

data class CSQuadruple<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
) : Serializable {
    override fun toString(): String = "($first, $second, $third, $fourth)"
}

infix fun <A, B, C, D> Triple<A, B, C>.to(that: D): CSQuadruple<A, B, C, D> =
    CSQuadruple(first, second, third, that)

data class CSQuintuple<out A, out B, out C, out D, out E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
) : Serializable {
    override fun toString(): String = "($first, $second, $third, $fourth, $fifth)"
}

infix fun <A, B, C, D, E> CSQuadruple<A, B, C, D>.to(that: E): CSQuintuple<A, B, C, D, E> =
    CSQuintuple(first, second, third, fourth, that)


data class CSSixtuple<out A, out B, out C, out D, out E, out F>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E,
    val sixth: F
) : Serializable {
    override fun toString(): String = "($first, $second, $third, $fourth, $fifth, $sixth)"
}

infix fun <A, B, C, D, E, F> CSQuintuple<A, B, C, D, E>.to(that: F): CSSixtuple<A, B, C, D, E, F> =
    CSSixtuple(first, second, third, fourth, fifth, that)

data class CSSeventuple<out A, out B, out C, out D, out E, out F, out G>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E,
    val sixth: F,
    val seventh: G
) : Serializable {
    override fun toString(): String = "($first, $second, $third, $fourth, $fifth, $sixth, $seventh)"
}

infix fun <A, B, C, D, E, F, G> CSSixtuple<A, B, C, D, E, F>.to(that: G)
        : CSSeventuple<A, B, C, D, E, F, G> =
    CSSeventuple(first, second, third, fourth, fifth, sixth, that)
