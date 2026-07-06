@file:Suppress("NOTHING_TO_INLINE")

package renetik.android.core.lang.value

@JvmName("intPlusCSValueInt")
inline operator fun Int.plus(value: CSValue<Int>): Int = this + value.value

@JvmName("intPlusCSValueFloat")
inline operator fun Int.plus(value: CSValue<Float>): Float = this + value.value

@JvmName("csValueIntPlusCSValueInt")
inline operator fun CSValue<Int>.plus(value: CSValue<Int>): Int = value.value + value.value

@JvmName("csValueIntPlusCSValueLong")
inline operator fun CSValue<Int>.plus(value: CSValue<Long>): Long = value.value + value.value

@JvmName("csValueIntPlusCSValueFloat")
inline operator fun CSValue<Int>.plus(value: CSValue<Float>): Float = value.value + value.value

@JvmName("csValueIntPlusCSValueDouble")
inline operator fun CSValue<Int>.plus(value: CSValue<Double>): Double = value.value + value.value

inline operator fun CSValue<Int>.plus(value: Int): Int = this.value + value
inline operator fun CSValue<Int>.plus(value: Float): Float = this.value + value

@JvmName("longPlusCSValueLong")
inline operator fun Long.plus(value: CSValue<Long>): Long = this + value.value

@JvmName("longPlusCSValueDouble")
inline operator fun Long.plus(value: CSValue<Double>): Double = this + value.value

@JvmName("longPlusCSValueInt")
inline operator fun Long.plus(value: CSValue<Int>): Long = this + value.value

@JvmName("longPlusCSValueFloat")
inline operator fun Long.plus(value: CSValue<Float>): Float = this + value.value

@JvmName("csValueLongPlusCSValueLong")
inline operator fun CSValue<Long>.plus(value: CSValue<Long>): Long = value.value + value.value

@JvmName("csValueLongDivFloat")
inline operator fun CSValue<Long>.div(value: Float): Float = this.value / value

@JvmName("csValueLongDivDouble")
inline operator fun CSValue<Long>.div(value: Double): Double = this.value / value

@JvmName("csValueFloatDivDouble")
inline operator fun CSValue<Float>.div(value: Double): Double = this.value / value

@JvmName("intMinusCSValueInt")
inline operator fun Int.minus(value: CSValue<Int>): Int = this - value.value

@JvmName("floatMinusCSValueFloat")
inline operator fun Float.minus(value: CSValue<Float>): Float = this - value.value

@JvmName("intMinusCSValueFloat")
inline operator fun Int.minus(value: CSValue<Float>): Float = this - value.value

inline operator fun CSValue<Int>.minus(value: Int): Int = this.value - value
inline operator fun CSValue<Float>.minus(value: Int): Float = this.value - value
inline operator fun CSValue<Int>.minus(value: Float): Float = this.value - value

@JvmName("csValueIntMinusCSValueInt")
inline operator fun CSValue<Int>.minus(value: CSValue<Int>): Int = this.value - value.value

@JvmName("intTimesCSValueInt")
inline operator fun Int.times(value: CSValue<Int>): Int = this * value.value

@JvmName("intTimesCSValueFloat")
inline operator fun Int.times(value: CSValue<Float>): Float = this * value.value

@JvmName("intTimesCSValueLong")
inline operator fun Int.times(value: CSValue<Long>): Long = this * value.value

@JvmName("intTimesCSValueDouble")
inline operator fun Int.times(value: CSValue<Double>): Double = this * value.value

inline operator fun CSValue<Int>.times(value: Int): Int = this.value * value
inline operator fun CSValue<Int>.times(value: Float): Float = this.value * value

@JvmName("csValueFloatTimesFloat")
inline operator fun CSValue<Float>.times(value: Float): Float = this.value * value

@JvmName("csValueFloatTimesInt")
inline operator fun CSValue<Float>.times(value: Int): Float = this.value * value

inline operator fun Float.times(value: CSValue<Int>): Float = this * value.value

inline operator fun Double.times(value: CSValue<Int>): Double = this * value.value

@JvmName("csValueIntTimesCSValueInt")
inline operator fun CSValue<Int>.times(value: CSValue<Int>): Int = this.value * value.value

@JvmName("csValueIntTimesCSValueFloat")
inline operator fun CSValue<Int>.times(value: CSValue<Float>): Float = this.value * value.value

@JvmName("intDivCSValueInt")
inline operator fun Int.div(value: CSValue<Int>): Int = this / value.value

@JvmName("intDivCSValueFloat")
inline operator fun Int.div(value: CSValue<Float>): Float = this / value.value

inline operator fun CSValue<Int>.div(value: Int): Int = this.value / value

@JvmName("csValueIntDivFloat")
inline operator fun CSValue<Int>.div(value: Float): Float = this.value / value

@JvmName("csValueIntDivDouble")
inline operator fun CSValue<Int>.div(value: Double): Double = this.value / value

inline operator fun CSValue<Int>.compareTo(value: Int): Int =
    this.value.compareTo(value)

inline operator fun Int.compareTo(value: CSValue<Int>): Int =
    this.compareTo(value.value)

inline operator fun CSValue<Int>.compareTo(value: CSValue<Int>): Int =
    this.value.compareTo(value.value)

@JvmName("csValueFloatCompareToInt")
inline operator fun CSValue<Float>.compareTo(value: Int): Int =
    this.value.compareTo(value)

@JvmName("csValueFloatCompareToCSValueInt")
inline operator fun CSValue<Float>.compareTo(value: CSValue<Int>): Int =
    this.value.compareTo(value.value)


