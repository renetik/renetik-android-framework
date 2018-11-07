package cs.android.extensions.primitives

fun String.asLong(): Long? {
    return try {
        toLong()
    } catch (ex: NumberFormatException) {
        null
    }
}

fun String.asFloat(): Float? {
    return try {
        toFloat()
    } catch (ex: NumberFormatException) {
        null
    }
}

fun String.asInt(): Int? {
    return try {
        toInt()
    } catch (ex: NumberFormatException) {
        null
    }
}

fun String.asDouble(default: Double): Double {
    return try {
        toDouble()
    } catch (ex: NumberFormatException) {
        default
    }
}

fun String.asLong(default: Long): Long {
    return try {
        toLong()
    } catch (ex: NumberFormatException) {
        default
    }
}


fun String.asFloat(default: Float): Float {
    return try {
        toFloat()
    } catch (ex: NumberFormatException) {
        default
    }
}

fun String.asInt(default: Int): Int {
    return try {
        toInt()
    } catch (ex: NumberFormatException) {
        default
    }
}