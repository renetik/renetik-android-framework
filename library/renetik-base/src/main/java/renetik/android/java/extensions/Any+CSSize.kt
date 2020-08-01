package renetik.android.java.extensions

import renetik.android.java.common.CSSizeInterface
import renetik.android.java.common.CSValue
import kotlin.math.roundToInt

//TODO: size define as float so double and float don't get rounded ? or make size private and use just for isEmpty isSet
val Any?.size: Int
    get() = when {
        this == null -> 0
        this is Int -> this
        this is Number -> toFloat().run { if (this > 0 && this < 1) 1 else roundToInt() }
        this is Boolean -> if (this) 1 else 0
        this is CharSequence -> this.length
        this is Collection<*> -> this.size
        this is Map<*, *> -> this.size
        this is CSValue<*> -> this.value.size
        this is CSSizeInterface -> this.size
        this is Array<*> -> this.size
        this is IntArray -> this.size
        this is DoubleArray -> this.size
        this is LongArray -> this.size
        this is CharArray -> this.size
        this is FloatArray -> this.size
        this is BooleanArray -> this.size
        this is ByteArray -> this.size
        else -> 1
    }

val Any?.isEmpty get() = size == 0

val Any?.isSet get() = !isEmpty

val Any?.notEmpty get() = isSet

fun <T : Any?> T.isSet(block: (T) -> Unit): Boolean = if (this.isSet) {
    block(this)
    true
} else false

fun <T : Any?> T.isEmpty(block: (T) -> Unit): Boolean = if (this.isEmpty) {
    block(this)
    true
} else false
