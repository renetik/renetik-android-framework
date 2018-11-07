package renetik.android;

import java.util.Locale;

public class CSLocale {

	public static boolean isMetric() {
		return Locale.getDefault() != Locale.US;
	}

	public static double toCelsius(int fahrenheit) {
		return (fahrenheit - 32) / 1.8;
	}

	public static double toFahrenheit(int celsius) {
		return celsius * 1.8 + 32;
	}

	public static double fromCelsiusToLocale(int celsius) {
		return isMetric() ? celsius : toFahrenheit(celsius);
	}

	public static double fromFahrenheitToLocale(int fahrenheit) {
		return !isMetric() ? fahrenheit : toCelsius(fahrenheit);
	}

}
