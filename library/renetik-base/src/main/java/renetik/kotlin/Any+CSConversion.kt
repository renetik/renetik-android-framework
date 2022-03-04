package renetik.kotlin

import renetik.android.framework.common.catchAllWarnReturn
import renetik.android.framework.lang.CSHasTitle
import renetik.android.framework.lang.CSValue
import renetik.android.primitives.isEmpty

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