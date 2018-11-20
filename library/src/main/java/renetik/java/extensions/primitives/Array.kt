package renetik.java.extensions.primitives

import renetik.java.extensions.stringify
import renetik.java.collections.list

fun Array<Any>.asStrings() = list<String>().apply {
    for (index in indices) add(this@asStrings[index].stringify())
}.toTypedArray()