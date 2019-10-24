package renetik.android.java.extensions

import renetik.android.java.common.CSSizeInterface
import renetik.android.java.common.CSValueInterface
import kotlin.math.roundToInt

//TODO: size define as float so double and float don't get rounded ? or make size private and use just for isEmpty isSet

val Any?.size: Int
    get() {
        if (this == null) return 0
        if (this is Int) return this
        if (this is Number) return toFloat().run { if (this > 0 && this < 1) 1 else roundToInt() }
        if (this is Boolean) return if (this) 1 else 0
        if (this is CharSequence) return this.length
        if (this is Collection<*>) return this.size
        if (this is Map<*, *>) return this.size
        if (this is CSValueInterface<*>) return this.value.size
        if (this is CSSizeInterface) return this.size
        if (this is Array<*>) return this.size
        if (this is IntArray) return this.size
        if (this is DoubleArray) return this.size
        if (this is LongArray) return this.size
        if (this is CharArray) return this.size
        if (this is FloatArray) return this.size
        if (this is BooleanArray) return this.size
        if (this is ByteArray) return this.size
        return 1
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
