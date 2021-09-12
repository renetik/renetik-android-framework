package renetik.android.primitives

import renetik.android.framework.lang.CSComparisonConstants.Ascending
import renetik.android.framework.lang.CSComparisonConstants.Descending
import renetik.android.framework.lang.CSComparisonConstants.Equal
import renetik.kotlin.isNull
import renetik.kotlin.notNull

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