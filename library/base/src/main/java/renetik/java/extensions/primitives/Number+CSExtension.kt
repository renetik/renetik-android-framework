package renetik.java.extensions.primitives

import renetik.java.extensions.isNull
import renetik.java.extensions.notNull
import renetik.java.lang.CSLang.ASCENDING
import renetik.java.lang.CSLang.DESCENDING
import renetik.java.lang.CSLang.EQUAL

val Number.isEmpty get() = toFloat() == 0F

fun compare(x: Int?, y: Int?): Int {
    if (x.notNull && y.notNull) return x!!.compareTo(y!!)
    if (x.isNull && y.isNull) return EQUAL
    return if (x.isNull && y.notNull) ASCENDING else DESCENDING
}

fun compare(x: Float?, y: Float?): Int {
    if (x.notNull && y.notNull) return x!!.compareTo(y!!)
    if (x.isNull && y.isNull) return 0
    return if (x.isNull && y.notNull) ASCENDING else DESCENDING
}