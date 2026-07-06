package renetik.android.core.math

data class CSPoint<T : Number>(val x: T, val y: T) {
    companion object {
        fun <T : Number> point(x: T, y: T) = CSPoint(x, y)
    }
}