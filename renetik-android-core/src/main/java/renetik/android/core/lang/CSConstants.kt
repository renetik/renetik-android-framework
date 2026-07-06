@file:Suppress("ObjectPropertyName")

package renetik.android.core.lang

object CSStringConstants {
    const val Empty = ""

    //    const val Ellipsize = "…"
    val alphabet = ('A'..'Z').toList()
    const val Ellipsize = "..."
    const val Comma = ","
    const val Semicolon = ";"
    const val NewLine = "\n"
    const val Space = " "
    const val `$` = "$"
    const val UnsafeFileChars = "|\\?*<\":>+[]/'"
}

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
    const val QuarterSecondL = QuarterSecond.toLong()
    const val HalfSecond = 500
    const val Second = 1000
    const val SecondL = Second.toLong()
    const val Minute = 60 * Second
    const val HalfMinute = Minute / 2
    const val Hour = 60 * Minute
    const val Day = 24 * Hour
    const val MilliToNano: Double = 1_000_000.0
    const val MilliToNanoInt: Int = 1_000_000
    const val SecondNano = Second * MilliToNanoInt
    fun Int.toNanosecond(): Int = this * MilliToNanoInt
    fun Double.toNanosecond(): Double = this * MilliToNano
}