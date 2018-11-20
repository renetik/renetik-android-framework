package renetik.java.extensions.primitives

import renetik.java.lang.CSLang

val CharSequence?.set get() = this?.let { it.length > 0 } ?: CSLang.NO
val CharSequence?.empty get() = !set