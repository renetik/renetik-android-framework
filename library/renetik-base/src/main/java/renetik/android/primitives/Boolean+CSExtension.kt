package renetik.android.primitives

val Boolean.Companion.random get() = Int.random(0, 1) == 1

fun Boolean.ifIs(function: () -> Unit) {
    if (this) function()
}

fun Boolean.ifNot(function: () -> Unit) {
    if (!this) function()
}

val <T : Boolean?> T.isTrue: Boolean get() = this == true
val <T : Boolean?> T.isNotTrue: Boolean get() = this != true

val <T : Boolean?> T.isFalse: Boolean get() = this == false