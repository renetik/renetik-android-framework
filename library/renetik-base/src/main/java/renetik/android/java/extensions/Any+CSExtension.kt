package renetik.android.java.extensions

import renetik.android.java.common.CSName

fun string(value: Any?) = value.stringify()

fun Any?.stringify(): String {
    val name = this as? CSName
    return name?.name ?: this?.toString() ?: ""
}

val Any?.asString get() = stringify()

@Suppress("UNCHECKED_CAST")
fun <Type : Any> Any.asType(): Type? = this as? Type

fun <T> T.init(block: T.() -> Unit) = apply { block() }

fun <T, ArgumentType : Any> T.init(argument: ArgumentType, block: T.(ArgumentType) -> Unit) =
    apply { block(argument) }

