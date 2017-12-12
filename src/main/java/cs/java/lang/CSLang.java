package cs.java.lang;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cs.android.CSAndroidApplication;
import cs.android.json.CSJSON;
import cs.android.json.CSJSONObject;
import cs.android.json.CSJSONType;
import cs.android.lang.CSApplication;
import cs.android.lang.CSDoLater;
import cs.android.lang.CSWork;
import cs.android.model.CSSettings;
import cs.java.callback.CSRun;
import cs.java.callback.CSRunWith;
import cs.java.collections.CSIteration;
import cs.java.collections.CSIterator;
import cs.java.collections.CSLinkedMapImpl;
import cs.java.collections.CSList;
import cs.java.collections.CSListImpl;
import cs.java.collections.CSListIterator;
import cs.java.collections.CSMap;
import cs.java.collections.CSMapImpl;
import cs.java.collections.CSMapIterator;
import cs.java.collections.CSMapped;
import cs.java.event.CSEvent;
import cs.java.event.CSEventImpl;

import static cs.android.json.CSJSON.toJSONString;

public class CSLang {

    public final static int ASCENDING = -1;
    public final static int EQUAL = 0;
    public final static int DESCENDING = 1;
    public final static int KB = 1024;
    public final static int MB = 1024 * KB;
    public static final boolean YES = true;
    public static final boolean NO = false;
    public static final int HALF_SECOND = 500;
    public static final int SECOND = HALF_SECOND * 2;
    public static final int MINUTE = 60 * SECOND;
    public static final int HOUR = 60 * MINUTE;
    public static final int DAY = 24 * HOUR;
    public static final Object INVOKE_FAILED = "invoke_failed";
    public static final String NEWLINE = "\n";
    private static final String DEBUG_MODE = "DEBUG_MODE";
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static CSApplication _application;

    public static boolean isDebugBuild() {
        return _application.isDebugBuild();
    }

    public static void setDebug(boolean value) {
        CSSettings.get().save(DEBUG_MODE, value);
    }

    public static <T extends CSID> String toIDs(List<T> hasIds) {
        CSList<String> IDs = list();
        for (CSID hasId : hasIds) IDs.add(hasId.id());
        return toJSONString(IDs);
    }

    public static <T extends CSID> T findById(CSValues<T> listData, String id) {
        if (no(listData)) return null;
        for (T hasId : listData.values()) if (equal(hasId.id(), id)) return hasId;
        return null;
    }

    public static <T extends CSID> T findById(CSValues<T> listData, CSID hasId) {
        return findById(listData, hasId.id());
    }

    public static <T extends CSID> int findIndexById(CSValues<T> listData, CSID hasId) {
        return is(hasId) ? findIndexById(listData, hasId.id()) : -1;
    }

    public static <T extends CSID> int findIndexById(CSValues<T> listData, String id) {
        if (is(listData))
            for (int index = 0; index < listData.values().size(); index++)
                if (equal(listData.values().get(index).id(), id)) return index;
        return -1;
    }

    public static <T extends CSName> CSList<String> toNames(CSValues<T> listData) {
        return toNames(is(listData) ? listData.values() : CSLang.<T>list());
    }

    private static <T extends CSName> CSList<String> toNames(List<T> listData) {
        CSList<String> names = list();
        for (CSName hasName : listData) names.add(hasName.name());
        return names;
    }

    public static String id(CSID hasId) {
        return is(hasId) ? hasId.id() : "";
    }

    public static void alert(Object... messages) {
        Toast.makeText(CSAndroidApplication.instance(), string(" ", strings(messages)), Toast.LENGTH_LONG).show();
        application().logger().info(messages);
    }

    public static void alertWarn(Object... messages) {
        Toast.makeText(CSAndroidApplication.instance(), "Warn: " + string(" ", strings(messages)),
                Toast.LENGTH_LONG).show();
        application().logger().warn(messages);
    }

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static long copyLarge(Reader input, Writer output) throws IOException {
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static int copy(Reader input, Writer output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static void close(Closeable closeable) {
        try {
            if (is(closeable)) closeable.close();
        } catch (IOException ioe) {
            error(ioe);
        }
    }

    public static void copy(InputStream input, Writer output)
            throws IOException {
        InputStreamReader in = new InputStreamReader(input);
        copy(in, output);
    }

    public static CSApplication application() {
        return _application;
    }

    public static String string(String separator, String... values) {
        CSTextInterface text = text();
        for (String string : values) if (set(string)) text.add(string).add(separator);
        if (!text.isEmpty()) text.deleteLast(separator.length());
        return text.toString();
    }

    public static String[] strings(Object[] objects) {
        String[] strings = new String[objects.length];
        for (int index = 0; index < objects.length; index++) {
            strings[index] = string(objects[index]);
        }
        return strings;
    }

    public static CSTextInterface text(String... values) {
        return new CSText(values);
    }

    public static String string(Object value) {
        return String.valueOf(value);
    }

    public static boolean androidMinimum(int verCode) {
        if (android.os.Build.VERSION.RELEASE.startsWith("1.0")) return verCode == 1;
        else if (android.os.Build.VERSION.RELEASE.startsWith("1.1")) return verCode <= 2;
        else if (android.os.Build.VERSION.RELEASE.startsWith("1.5")) return verCode <= 3;
        else return android.os.Build.VERSION.SDK_INT >= verCode;
    }

    public static String[] toStringArray(Collection<String> list) {
        return list.toArray(new String[list.size()]);
    }

    public static <T> T as(Object object, Class<T> expectedClass) {
        try {
            return (T) object;
        } catch (Exception e) {
            warn(e);
        }
        return null;
    }

    public static double asDouble(Object value) {
        try {
            return Double.parseDouble(string(value));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static boolean asBool(Object value) {
        return Boolean.parseBoolean(string(value));
    }

    public static Integer asInt(Object value) {
        try {
            return Integer.parseInt(string(value));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String asString(InputStream input) {
        CSStringBuilderWriter sw = new CSStringBuilderWriter();
        try {
            copy(input, sw);
        } catch (IOException e) {
            error(e);
            return "";
        }
        return sw.toString();
    }

    public static void error(Throwable e, Object... values) {
        application().logger().error(e, values);
    }

    public static boolean is(Object item) {
        return item != null;
    }

    public static String asString(Intent value) {
        String string = value.toString();
        if (is(value.getExtras())) {
            string += " Extras: ";
            for (String key : value.getExtras().keySet()) {
                Object info = value.getExtras().get(key);
                string += String.format("%s %s (%s)", key, info, is(info) ? info.getClass().getName() : "");
            }
        }
        return string;
    }

    public static boolean between(int value, int from, int to) {
        return value >= from && value < to;
    }

    public static void close(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String createTraceString(Throwable throwable) {
        if (no(throwable)) return "";
        return Log.getStackTraceString(throwable);
    }

    public static boolean no(Object object) {
        return object == null;
    }

    public static long currentTime() {
        return new Date().getTime();
    }

    public static CSDoLater doLater(int delayMilliseconds, final Runnable runnable) {
        return new CSDoLater(runnable, delayMilliseconds);
    }

    public static CSDoLater doLater(final Runnable runnable) {
        return new CSDoLater(runnable);
    }

    public static boolean empty(Object... objects) {
        for (Object object : objects)
            if (!empty(object)) return false;
        return true;
    }

    public static boolean empty(Object object) {
        if (object == null) return YES;
        if (object instanceof Number) return ((Number) object).floatValue() == 0;
        if (object instanceof Boolean) return !(Boolean) object;
        if (object instanceof CharSequence) return ((CharSequence) object).length() == 0;
        if (object instanceof Collection) return ((Collection<?>) object).size() == 0;
        if (object instanceof CSValueInterface<?>)
            return empty(((CSValueInterface<?>) object).get());
        if (object instanceof CSMap) return ((CSMap<?, ?>) object).size() == 0;
        if (object instanceof Object[]) return ((Object[]) object).length == 0;
        if (object instanceof int[]) return ((int[]) object).length == 0;
        if (object instanceof double[]) return ((double[]) object).length == 0;
        if (object instanceof long[]) return ((long[]) object).length == 0;
        if (object instanceof char[]) return ((char[]) object).length == 0;
        if (object instanceof float[]) return ((float[]) object).length == 0;
        if (object instanceof boolean[]) return ((boolean[]) object).length == 0;
        if (object instanceof byte[]) return ((byte[]) object).length == 0;
        return NO;
    }

    public static int size(Object object) {
        if (object == null) return 0;
        if (object instanceof Number) return ((Number) object).intValue();
        if (object instanceof CharSequence) return ((CharSequence) object).length();
        if (object instanceof Collection) return ((Collection<?>) object).size();
        if (object instanceof CSValueInterface<?>)
            return size(((CSValueInterface<?>) object).get());
        if (object instanceof CSMap) return ((CSMap<?, ?>) object).size();
        if (object instanceof Object[]) return ((Object[]) object).length;
        if (object instanceof int[]) return ((int[]) object).length;
        if (object instanceof double[]) return ((double[]) object).length;
        if (object instanceof long[]) return ((long[]) object).length;
        if (object instanceof char[]) return ((char[]) object).length;
        if (object instanceof float[]) return ((float[]) object).length;
        if (object instanceof boolean[]) return ((boolean[]) object).length;
        if (object instanceof byte[]) return ((byte[]) object).length;
        return 1;
    }

    public static boolean equalOne(Object object, Object... possibilities) {
        for (Object possibility : possibilities)
            if (equal(object, possibility)) return true;
        return false;
    }

    public static boolean equal(Object obj1, Object obj2) {
        if (obj1 == null) return obj2 == null;
        return obj1.equals(obj2);
    }

    public static void error(Object... values) {
        application().logger().error(values);
    }

    public static <T> CSEvent<T> event() {
        return new CSEventImpl<>();
    }

    public static RuntimeException exception(Object... values) {
        return new RuntimeException(string(" ", strings(values)));
    }

    public static <T> void fire(CSEvent<T> event, T argument) {
        event.fire(argument);
    }

    public static void fire(CSEvent<Void> eventVoid) {
        eventVoid.fire(null);
    }

    public static void fire(CSRun eventVoid) {
        if (is(eventVoid)) eventVoid.run();
    }

    public static <T> T first(T[] items) {
        return list(items).first();
    }

    public static <T> CSList<T> list(T... items) {
        CSList<T> list = list();
        add(list, items);
        return list;
    }

    public static <T> CSList<T> list() {
        return new CSListImpl<>();
    }

    public static <T> void add(CSList<T> list, T... items) {
        Collections.addAll(list, items);
    }

    public static String stringf(String format, Object... args) {
        return String.format(Locale.ENGLISH, format, args);
    }

    public static boolean has(String string, String... contents) {
        for (String content : contents)
            if (!string.contains(content)) return false;
        return true;
    }

    public static boolean hasOne(String string, String... contents) {
        for (String content : contents)
            if (string.contains(content)) return true;
        return false;
    }

    public static boolean instanceOf(Object object, Class... classes) {
        if (no(object)) return NO;
        for (Class type : classes)
            if (type.isAssignableFrom(object.getClass())) return YES;
        return false;
    }

    public static Object invoke(Object object, String methodName) {
        try {
            return object.getClass().getMethod(methodName, (Class<?>[]) null).invoke(object);
        } catch (Exception e) {
            return INVOKE_FAILED;
        }
    }

    public static Object invoke(Object object, String methodName, Class<?>[] types, Object[] argument) {
        try {
            return object.getClass().getMethod(methodName, types).invoke(object, argument);
        } catch (Exception e) {
            return INVOKE_FAILED;
        }
    }

    public static <T> Object invoke(Object object, String methodName, Class<T> type, T argument) {
        try {
            return object.getClass().getMethod(methodName, type).invoke(object, argument);
        } catch (Exception e) {
            return INVOKE_FAILED;
        }
    }

    public static Object invoke(String methodName, Class targetClass, Class[] paramTypes,
                                Object[] arguments) {
        try {
            Method method = targetClass.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            return method.invoke(null, arguments);
        } catch (Exception e) {
            return INVOKE_FAILED;
        }
    }

    public static boolean allIs(Object... items) {
        for (Object object : items) if (!is(object)) return NO;
        return YES;
    }

    public static CSIteration<Integer> iterate(int count) {
        return new CSIterator<Integer>(count) {
            public Integer getValue() {
                return index();
            }
        };
    }

    public static int[] iterate(int[] integers) {
        if (no(integers)) return new int[0];
        return integers;
    }

    public static <T> CSIterator<T> iterate(Iterable<T> items) {
        return new CSListIterator<>(list(items));
    }

    public static <T> CSList<T> list(Iterable<T> items) {
        CSList<T> list = list();
        if (is(items)) for (T item : items)
            list.add(item);
        return list;
    }

    public static <K, V> CSIteration<CSMapped<K, V>> iterate(java.util.Map<K, V> map) {
        return new CSMapIterator<>(map);
    }

    public static <T> CSIterator<T> iterate(T... items) {
        return iterate(list(items));
    }

    public static <T> CSIterator<T> iterate(CSList<T> items) {
        return new CSListIterator<>(list(items));
    }

    public static CSIteration<View> iterate(final ViewGroup layout) {
        return new CSIterator<View>(layout.getChildCount()) {
            public View getValue() {
                return layout.getChildAt(index());
            }
        };
    }

    public static String nullAsString(Object value) {
        if (no(value)) return "";
        return value + "";
    }

    public static CSJSONType json(String json_string) {
        return CSJSON.parse(json_string);
    }

    public static CSJSONObject json(Map data) {
        return CSJSON.create(data);
    }

    public static CSJSONObject jsonObject(String json_string) {
        return json(json_string).asObject();
    }

    public static <T> T last(T[] items) {
        return list(items).last();
    }

    public static <K, V> CSLinkedMapImpl<K, V> linkedMap() {
        return new CSLinkedMapImpl<>();
    }

    public static CSMap map(Object... values) {
        CSMap map = map();
        for (int i = 0; i < values.length; i += 2) map.put(values[i], values[i + 1]);
        return map;
    }

    public static <K, V> CSMap<K, V> map() {
        return new CSMapImpl<>();
    }

    public static CSMap<String, String> map(String... values) {
        CSMap<String, String> map = map();
        for (int i = 0; i < values.length; i += 2) map.put(values[i], values[i + 1]);
        return map;
    }

    public static <T> T newInstance(Class<T> itemsClass) {
        try {
            return itemsClass.newInstance();
        } catch (Exception e) {
            error(e);
            return null;
        }
    }

    public static boolean no(Object... objects) {
        if (objects == null) return true;
        for (Object object : objects)
            if (is(object)) return false;
        return true;
    }

    public static RuntimeException notImplemented(Object... msg) {
        return new RuntimeException(text("Unimplemented ").add(string(" ", strings(msg))).toString());
    }

    public static boolean respondsTo(Object object, String methodName) {
        try {
            object.getClass().getMethod(methodName, (Class<?>[]) null);
            return YES;
        } catch (NoSuchMethodException e) {
            return NO;
        }
    }

    public static CSWork schedule(final int delayMilliseconds, final CSRun runnable) {
        return new CSWork(delayMilliseconds, runnable);
    }

    public static boolean allSet(Object... objects) {
        for (Object value : objects) if (!set(value)) return NO;
        return YES;
    }

    public static boolean set(CharSequence value) {
        return value != null && value.length() > 0;
    }

    public static boolean set(Object value) {
        return !empty(value);
    }

    public static void setApplication(CSApplication application) {
        _application = application;
        CSDoLater.initialize();
    }

    public static void info(Object... values) {
        if (isDebugMode()) alert(values);
        else application().logger().info(values);
    }

    public static boolean isDebugMode() {
        return CSSettings.get().loadBoolean(DEBUG_MODE);
    }

    public static int to1E6(double value) {
        return (int) (value * 1E6);
    }

    public static RuntimeException unexpected(Object... msg) {
        return new RuntimeException(text("Unexpected ").add(string(" ", strings(msg))).toString());
    }

    public static String urlEncode(String argument) {
        try {
            return URLEncoder.encode(argument, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw exception(e);
        }
    }

    public static RuntimeException exception(Exception ex) {
        return new RuntimeException(ex);
    }

    public static void warn(Object... values) {
        application().logger().warn(values);
    }

    public static void warn(Throwable e, Object... values) {
        application().logger().warn(e, values);
    }

    public static Object newInstance(String className) {
        try {
            Class aClass = Class.forName(className);
            return newInstance(aClass);
        } catch (ClassNotFoundException e) {
            warn(e);
        }
        return null;
    }

    public static Throwable getRootCause(Throwable throwable) {
        List<Throwable> list = new ArrayList<Throwable>();
        while (throwable != null && !list.contains(throwable)) {
            list.add(throwable);
            throwable = throwable.getCause();
        }
        return list.size() < 2 ? null : list.get(list.size() - 1);
    }

    public static String getRootCauseMessage(Throwable throwable) {
        Throwable cause = getRootCause(throwable);
        return is(cause) ? cause.getMessage() : "";
    }

    public static <T> void run(CSRunWith<T> runWith, T date) {
        if (is(runWith)) runWith.run(date);
    }

    public static void run(CSRun run) {
        if (is(run)) run.run();
    }

    public static int compare(Integer x, Integer y) {
        return is(x) ? x.compareTo(y) : -1;
    }

    public static int compare(Float x, Float y) {
        return is(x) ? x.compareTo(y) : -1;
    }

}
