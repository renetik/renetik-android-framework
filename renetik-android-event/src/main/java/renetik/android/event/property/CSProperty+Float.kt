package renetik.android.event.property

fun CSProperty<Float>.min(value: Float) = apply {
    if (this.value < value) this.value(value, fire = false)
}

fun CSProperty<Float>.max(value: Float) = apply {
    if (this.value > value) this.value(value, fire = false)
}

fun CSProperty<Float>.range(range: ClosedRange<Float>) = apply {
    min(range.start).max(range.endInclusive)
}