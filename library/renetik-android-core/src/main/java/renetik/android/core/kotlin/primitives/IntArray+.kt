package renetik.android.core.kotlin.primitives

import java.lang.System.arraycopy

fun IntArray.rotate(offset: Int, start: Int = 0, end: Int = size) {
    require(start < end) { "startIndex must be less than endIndex" }
    require(offset in indices) {
        "Invalid offsetIndex: $offset not in $indices"
    }
    val array1 = sliceArray(start until offset)
    val array2 = sliceArray(offset until end)
    arraycopy(array2, 0, this, start, array2.size)
    arraycopy(array1, 0, this, start + array2.size, array1.size)
}

fun FloatArray.rotate(offset: Int, start: Int = 0, end: Int = size) {
    require(start < end) { "startIndex must be less than endIndex" }
    require(offset in indices) {
        "Invalid offsetIndex: $offset not in $indices"
    }
    val array1 = sliceArray(start until offset)
    val array2 = sliceArray(offset until end)
    arraycopy(array2, 0, this, start, array2.size)
    arraycopy(array1, 0, this, start + array2.size, array1.size)
}

fun Pair<FloatArray, FloatArray>.rotate(offset: Int, start: Int = 0) {
    first.rotate(offset, start)
    second.rotate(offset, start)
}