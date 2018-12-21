package renetik.java.collections

import renetik.java.lang.CSIValueInterface

interface CSIterated<T> : CSIValueInterface<T> {
    val next: T

    val previous: T

    fun index(): Int

    fun remove()
}
