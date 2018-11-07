package renetik.android.lang;

public interface CSLogger {

	void onLowMemory();

	void error(Object... values);

	void error(Throwable e, Object... values);

	void info(Object... values);

	void debug(Object... values);

	String logString();

	void warn(Object... values);

	void warn(Throwable e, Object... values);

}
