package renetik.android.core.kotlin.primitives

import renetik.android.core.lang.CSComparisonConstants.Ascending
import renetik.android.core.lang.CSComparisonConstants.Descending
import renetik.android.core.lang.CSComparisonConstants.Equal
import renetik.android.core.kotlin.isNull
import renetik.android.core.kotlin.notNull

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