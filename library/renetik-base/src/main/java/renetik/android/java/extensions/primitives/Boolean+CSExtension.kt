package renetik.android.java.extensions.primitives

import renetik.android.java.extensions.primitives.IntCSExtension.randomIntInRange

object BooleanCSExtension {
    fun randomBoolean() = randomIntInRange(0, 1) == 1
}

fun Boolean.ifIs(function: () -> Unit) {
    if (this) function()
}

fun Boolean.ifNot(function: () -> Unit) {
    if (!this) function()
}

val <T : Boolean?> T.isTrue: Boolean get() = this == true

val <T : Boolean?> T.isFalse: Boolean get() = this == false