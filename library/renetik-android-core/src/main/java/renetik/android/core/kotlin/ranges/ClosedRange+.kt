package renetik.android.core.kotlin.ranges

val ClosedRange<Float>.size: Float get() = endInclusive - start
val ClosedRange<Float>.first: Float get() = start
val ClosedRange<Float>.last: Float get() = endInclusive

val ClosedRange<Int>.size: Int get() = endInclusive - first
val ClosedRange<Int>.first: Int get() = start
val ClosedRange<Int>.last: Int get() = endInclusive

val ClosedRange<Double>.size: Double get() = endInclusive - start
val ClosedRange<Double>.first: Double get() = start
val ClosedRange<Double>.last: Double get() = endInclusive
