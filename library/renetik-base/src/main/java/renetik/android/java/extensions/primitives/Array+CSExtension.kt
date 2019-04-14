package renetik.android.java.extensions.primitives

import renetik.android.java.extensions.stringify
import renetik.android.java.extensions.collections.list

fun Array<Any>.asStrings() = list<String>().apply {
    for (index in indices) add(this@asStrings[index].stringify())
}.toTypedArray()