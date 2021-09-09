package renetik.android.primitives

val Float.Companion.empty get() = MAX_VALUE
val Float.isEmpty get() = this == Float.empty
val Float.isSet get() = !this.isEmpty
fun Float.ifEmpty(function: (Float) -> Unit) = apply {
    if (this.isEmpty) function(this)
}

fun Float.ifSet(function: (Float) -> Unit) = apply {
    if (this.isSet) function(this)
}


