package renetik.android.java.extensions.primitives

import renetik.android.java.extensions.isNull
import renetik.android.java.extensions.notNull
import renetik.android.java.common.CSConstants.ASCENDING
import renetik.android.java.common.CSConstants.DESCENDING
import renetik.android.java.common.CSConstants.EQUAL

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