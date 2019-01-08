package renetik.android.java.extensions.primitives

fun Boolean.ifIs(function: () -> Unit) {
    if (this) function()
}

fun Boolean.ifNot(function: () -> Unit) {
    if (!this) function()
}

val <T : Boolean?> T.isTrue: Boolean get() = this == true