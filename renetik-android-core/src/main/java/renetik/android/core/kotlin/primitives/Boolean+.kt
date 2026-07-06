package renetik.android.core.kotlin.primitives

import renetik.android.core.lang.Fun

val Boolean.Companion.random get() = Int.random(0, 1) == 1

val Boolean?.isTrue: Boolean get() = this == true
val Boolean?.isFalse: Boolean get() = this == false
val Boolean?.isFalseOrNull: Boolean get() = this == null || this == false

infix fun Boolean.or(boolean: Boolean?) = isTrue || boolean.isTrue
infix fun Boolean?.or(boolean: Boolean) = isTrue || boolean
infix fun Boolean.and(boolean: Boolean?) = isTrue && boolean.isTrue
infix fun Boolean?.and(boolean: Boolean) = isTrue && boolean

inline fun <T : Boolean?> T.isTrue(function: Fun) {
    if (isTrue) function()
}

inline fun <T : Boolean?> T.isFalse(function: Fun) {
    if (isFalse) function()
}

inline fun <T : Boolean?, R> T.ifTrue(function: () -> R): R? =
    if (isTrue) function() else null

inline fun <T : Boolean?, R> T.ifFalse(function: () -> R): R? =
    if (isFalse) function() else null