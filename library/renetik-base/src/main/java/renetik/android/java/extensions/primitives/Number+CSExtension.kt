package renetik.android.java.extensions.primitives

import renetik.android.java.common.CSComparisionConstants.Ascending
import renetik.android.java.common.CSComparisionConstants.Descending
import renetik.android.java.common.CSComparisionConstants.Equal
import renetik.android.java.extensions.isNull
import renetik.android.java.extensions.notNull

val Number.isEmpty get() = toFloat() == 0F

fun compare(x: Int?, y: Int?): Int {
    if (x.notNull && y.notNull) return x!!.compareTo(y!!)
    if (x.isNull && y.isNull) return Equal
    return if (x.isNull && y.notNull) Ascending else Descending
}

fun compare(x: Float?, y: Float?): Int {
    if (x.notNull && y.notNull) return x!!.compareTo(y!!)
    if (x.isNull && y.isNull) return 0
    return if (x.isNull && y.notNull) Ascending else Descending
}