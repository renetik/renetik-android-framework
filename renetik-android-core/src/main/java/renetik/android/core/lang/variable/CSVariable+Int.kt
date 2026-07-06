package renetik.android.core.lang.variable

fun CSVariable<Int>.increment() {
    value += 1
}

fun CSVariable<Int>.decrement() {
    value -= 1
}

operator fun CSVariable<Int>.minusAssign(value: Int) {
    this.value -= value
}

operator fun CSVariable<Int>.plusAssign(value: Int) {
    this.value += value
}