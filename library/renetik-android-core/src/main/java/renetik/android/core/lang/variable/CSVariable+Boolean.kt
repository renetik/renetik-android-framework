package renetik.android.core.lang.variable

fun CSVariable<Boolean>.setTrue() {
    this.value = true
}

fun CSVariable<Boolean>.setFalse() {
    this.value = false
}

fun CSVariable<Boolean>.toggle() {
    value = !value
}

fun CSSafeVariable<Boolean>.toggle() {
    while (true) {
        val current = value
        if (compareAndSet(current, !current)) return
    }
}