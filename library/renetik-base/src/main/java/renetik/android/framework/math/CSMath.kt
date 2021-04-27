package renetik.android.framework.math

object CSMath {
    fun to1E6(value: Double) = (value * 1E6).toInt()
    fun between(value: Int, from: Int, to: Int) = value in from until to
    fun getDistance(point1: CSPoint, point2: CSPoint) =
            Math.sqrt(((point1.x() - point2.x()) * (point1.x() - point2.x()) + (point1.y()
                    - point2.y()) * (point1.y() - point2.y())).toDouble()).toFloat()

    fun randomInt(minimum: Int, maximum: Int) = minimum + (Math.random() * maximum).toInt()
}
