package renetik.android.core.lang.value

operator fun <T> CSValue<List<T>>.get(index: Int): T = value[index]