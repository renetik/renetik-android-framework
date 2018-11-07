package cs.android.util;

import static cs.java.lang.CSLang.error;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CSDate {

	public static Date dateFromString(String format, String string) {
		try {
			return new SimpleDateFormat(format).parse("" + string);
		} catch (ParseException e) {
			error(e);
		}
		return new Date();
	}

	public static int addCalendarField(int field, int add) {
		Calendar instance = Calendar.getInstance();
		instance.add(field, add);
		return instance.get(field);
	}

	public static String stringFromDate(String format, Date date) {
		return new SimpleDateFormat(format).format(date);
	}

	public static Date addYears(Date date, int value) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.add(Calendar.YEAR, value);
		return instance.getTime();
	}

}
