package renetik.android.primitives

import renetik.android.primitives.CSInt.random

object CSBoolean {
    val random get() = random(0, 1) == 1
}

fun Boolean.ifIs(function: () -> Unit) {
    if (this) function()
}

fun Boolean.ifNot(function: () -> Unit) {
    if (!this) function()
}

val <T : Boolean?> T.isTrue: Boolean get() = this == true
val <T : Boolean?> T.isNotTrue: Boolean get() = this != true

val <T : Boolean?> T.isFalse: Boolean get() = this == false