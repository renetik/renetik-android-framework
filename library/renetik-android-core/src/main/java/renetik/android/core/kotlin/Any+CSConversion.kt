package renetik.android.core.kotlin

import renetik.android.core.kotlin.primitives.isEmpty
import renetik.android.core.lang.CSHasTitle
import renetik.android.core.lang.CSValue
import renetik.android.core.lang.catchAllWarnReturn

@Suppress("UNCHECKED_CAST")
fun <Type : Any> Any.type() = (this as Type)

val Any?.asString
    get() = (this as? CSHasTitle)?.title
        ?: (this as? CSValue<*>)?.value as? String
        ?: this?.let { "$it" } ?: ""

val List<*>.asStringArray get() = asStringList.toTypedArray()

val List<*>.asStringList: List<String> get() = map { it.asString }

val Any?.asInt
    get() = catchAllWarnReturn(0) {
        val value = asString
        if (value.isEmpty) 0 else value.toInt()
    }