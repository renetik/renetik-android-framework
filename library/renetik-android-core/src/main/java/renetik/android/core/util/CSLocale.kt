package renetik.android.core.util

import java.util.*

object CSLocale {

    val isMetric: Boolean
        get() = Locale.getDefault() !== Locale.US

    fun toCelsius(fahrenheit: Int): Double {
        return (fahrenheit - 32) / 1.8
    }

    fun toFahrenheit(celsius: Int): Double {
        return celsius * 1.8 + 32
    }

    fun fromCelsiusToLocale(celsius: Int): Double {
        return if (isMetric) celsius.toDouble() else toFahrenheit(celsius)
    }

    fun fromFahrenheitToLocale(fahrenheit: Int): Double {
        return if (!isMetric) fahrenheit.toDouble() else toCelsius(fahrenheit)
    }

}
