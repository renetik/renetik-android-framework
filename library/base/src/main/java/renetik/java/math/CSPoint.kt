package renetik.java.math

class CSPoint(val x: Number, val y: Number) {

    fun doubleX() = x.toDouble()
    fun doubleY() = y.toDouble()
    fun x() = x.toFloat()
    fun y() = y.toFloat()
    override fun toString() = "[$x;$y]"

    fun equals(point: CSPoint) = x() == point.x() && y() == point.y()

    override fun equals(other: Any?) = if (other is CSPoint) equals(other) else super.equals(other)
}
