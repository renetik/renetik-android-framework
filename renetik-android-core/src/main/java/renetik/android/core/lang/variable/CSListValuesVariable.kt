package renetik.android.core.lang.variable

interface CSListValuesVariable<T> : CSVariable<T> {
    val values: List<T>
}