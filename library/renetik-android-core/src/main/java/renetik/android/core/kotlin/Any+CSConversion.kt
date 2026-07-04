package renetik.android.core.kotlin

import renetik.android.core.kotlin.primitives.asDouble
import renetik.android.core.kotlin.primitives.asFloat
import renetik.android.core.kotlin.primitives.asInt
import renetik.android.core.kotlin.primitives.asLong
import renetik.android.core.lang.CSHasTitle
import renetik.android.core.lang.value.CSValue

val Any?.asString
    get() = (this as? CSHasTitle)?.title
        ?: (this as? CSValue<*>)?.value as? String
        ?: this?.let { "$it" } ?: ""

val List<*>.asStringArray get() = asStringList.toTypedArray()
val List<*>.asStringList: List<String> get() = map { it.asString }

fun Any.asInt(): Int? = asString.asInt()
fun Any.asLong(): Long? = asString.asLong()
fun Any.asFloat(): Float? = asString.asFloat()
fun Any.asDouble(): Double? = asString.asDouble()
fun Any.asBoolean(): Boolean = asString.toBoolean()