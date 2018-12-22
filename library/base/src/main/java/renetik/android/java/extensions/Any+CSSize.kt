package renetik.android.java.extensions

import renetik.android.java.collections.CSMap
import renetik.android.java.common.CSValueInterface
import renetik.android.java.common.CSSizeInterface

val Any?.size: Int
    get() { ///TODO tests for this please so its more clear what it returns in some situations :)
        if (this == null) return 0
        if (this is Int) return this
        if (this is Number) return "$this".length
        if (this is Boolean) return if (this) 1 else 0
        if (this is CharSequence) return this.length
        if (this is Collection<*>) return this.size
        if (this is CSValueInterface<*>) return this.value.size
        if (this is CSSizeInterface) return this.size
        if (this is CSMap<*, *>) return this.size
        if (this is Array<*>) return this.size
        if (this is IntArray) return this.size
        if (this is DoubleArray) return this.size
        if (this is LongArray) return this.size
        if (this is CharArray) return this.size
        if (this is FloatArray) return this.size
        if (this is BooleanArray) return this.size
        if (this is ByteArray) return this.size
        return 1;
    }

val Any?.isEmpty get() = size == 0

val Any?.isSet get() = !isEmpty

fun set(value: Any?) = value.isSet

fun size(value: Any?) = value.size

fun empty(value: Any?) = value.isEmpty
