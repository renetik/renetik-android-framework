package renetik.android.core.kotlin

import java.lang.System.arraycopy

fun FloatArray.overwrite(position: Int, data: FloatArray): FloatArray =
    if (position + data.size <= size) {
        arraycopy(data, 0, this, position, data.size)
        this
    } else {
        val newArray = copyOf(maxOf(size, position + data.size))
        arraycopy(data, 0, newArray, position, data.size)
        newArray
    }