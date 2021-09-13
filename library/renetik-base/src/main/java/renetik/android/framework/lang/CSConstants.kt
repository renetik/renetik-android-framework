package renetik.android.framework.lang

object CSDataConstants {
    const val KB = 1024
    const val MB = 1024 * KB
    const val GB = 1024 * MB
}

object CSComparisonConstants {
    const val Ascending = -1
    const val Equal = 0
    const val Descending = 1
}

object CSTimeConstants {
    const val QuarterSecond = 250
    const val HalfSecond = 500
    const val Second = 1000
    const val Minute = 60 * Second
    const val Hour = 60 * Minute
    const val Day = 24 * Hour
    const val MilliToNanoSecondMultiplier = 1000000
    fun Int.toNanosecond() = this * MilliToNanoSecondMultiplier
}