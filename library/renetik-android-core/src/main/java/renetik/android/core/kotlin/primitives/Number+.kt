package renetik.android.core.kotlin.primitives

import renetik.android.core.lang.CSComparisonConstants.Ascending
import renetik.android.core.lang.CSComparisonConstants.Descending
import renetik.android.core.lang.CSComparisonConstants.Equal

fun compare(x: Int?, y: Int?): Int {
    if (x != null && y != null) return x.compareTo(y)
    if (x == null && y == null) return Equal
    return if (x == null) Ascending else Descending
}

fun compare(x: Float?, y: Float?): Int {
    if (x != null && y != null) return x.compareTo(y)
    if (x == null && y == null) return 0
    return if (x == null) Ascending else Descending
}