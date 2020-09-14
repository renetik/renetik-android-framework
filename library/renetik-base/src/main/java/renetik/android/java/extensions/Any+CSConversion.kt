package renetik.android.java.extensions

import renetik.android.java.common.CSName
import renetik.android.java.common.CSValue
import renetik.android.java.common.catchAllWarnReturn

fun Any?.asString(): String {
    return (this as? CSName)?.name
        ?: (this as? CSValue<*>)?.value as? String
        ?: this?.let { "$it" } ?: ""
}

val List<*>.asStringArray get() = asStringList.toTypedArray()

val List<*>.asStringList: List<String> get() = map { it.asString() }

fun Any?.asInt() = catchAllWarnReturn(0) { asString().toInt() }

