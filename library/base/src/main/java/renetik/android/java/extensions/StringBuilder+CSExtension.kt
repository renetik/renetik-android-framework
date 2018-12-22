package renetik.android.java.extensions

import renetik.android.java.collections.CSList
import renetik.android.java.collections.list
import renetik.android.java.common.CSConstants.NEWLINE

fun StringBuilder(vararg values: String) = kotlin.text.StringBuilder().add(*values)
val StringBuilder.isEmpty get() = length == 0
fun StringBuilder.add(vararg strings: CharSequence) = apply { for (string in strings) append(string) }
fun StringBuilder.add(vararg objects: Any) = apply { for (any in objects) add(any.toString()) }
fun StringBuilder.addLine() = apply { add(NEWLINE) }
fun StringBuilder.addSpace() = apply { add(" ") }
fun StringBuilder.caseDown() = apply { reload(toString().toLowerCase()) }

fun StringBuilder.caseUp(atIndex: Int) = apply {
    reload(substring(0, atIndex) + substring(atIndex, atIndex + 1).toUpperCase()
            + substring(atIndex + 1, length))
}

fun StringBuilder.cut(startIndex: Int, endIndex: Int) = apply {
    var cutEndIndex = endIndex
    if (startIndex >= length) if (cutEndIndex > length) cutEndIndex = length
    delete(startIndex, cutEndIndex)
}

fun StringBuilder.remove(start: Int, length: Int) = apply { cut(start, start + length) }
fun StringBuilder.deleteLast(length: Int) = apply { cut(this.length - length, this.length) }
fun StringBuilder.leaveStart(length: Int) = apply { cut(length, this.length) }
fun StringBuilder.cutStart(length: Int) = apply { cut(0, length) }

fun StringBuilder.remove(vararg strings: String) = apply {
    var text = toString()
    for (string in strings) text = text.replace(string.toRegex(), "")
    reload(text)
}

fun StringBuilder.replace(regex: String, replace: String) = apply {
    reload(toString().replace(regex.toRegex(), replace))
}

fun StringBuilder.replaceEnd(string: String) = apply {
    deleteLast(string.length)
    add(string)
}

fun StringBuilder.split(regex: String): CSList<StringBuilder> {
    val split = list<StringBuilder>()
    for (string in toString().split(regex.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
        split.add(StringBuilder(string))
    return split
}

fun StringBuilder.trim() = apply { reload(toString().trim { it <= ' ' }) }

fun StringBuilder.reload(text: CharSequence) {
    clear()
    append(text)
}

private fun StringBuilder.clear() = apply { delete(0, length) }

fun StringBuilder.removeEnd(value: String) = apply {
    val valueStart = lastIndexOf(value)
    if (valueStart > -1) remove(valueStart, value.length)
}

