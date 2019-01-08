package renetik.android.java.extensions

fun string(value: Any?) = value.stringify()
fun Any?.stringify() = this?.toString() ?: ""

@Suppress("UNCHECKED_CAST")
fun <Type : Any> Any.asType(): Type? = this as? Type

fun <T> T.init(block: T.() -> Unit) = apply { block() }
fun <T, ArgumentType : Any> T.init(argument: ArgumentType, block: T.(ArgumentType) -> Unit) = apply { block(argument) }

