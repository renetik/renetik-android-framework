package renetik.android.primitives

val Boolean.Companion.random get() = Int.random(0, 1) == 1

fun <T> Boolean.ifTrue(function: () -> T): T? = if (this) function() else null
fun <T> Boolean.ifFalse(function: () -> T): T? = if (!this) function() else null
val <T : Boolean?> T.isTrue: Boolean get() = this == true
val <T : Boolean?> T.isNotTrue: Boolean get() = this != true
val <T : Boolean?> T.isFalse: Boolean get() = this == false