package cs.android.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import androidx.annotation.Nullable;
import cs.android.json.CSJsonData;
import cs.android.viewbase.CSContextController;
import cs.java.collections.CSList;
import cs.java.collections.CSMap;
import cs.java.collections.CSMapItem;

import static cs.android.json.CSJsonKt.createList;
import static cs.android.json.CSJsonKt.toJson;
import static cs.java.lang.CSLang.empty;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.iterate;
import static cs.java.lang.CSLang.map;
import static cs.java.lang.CSLang.no;


public class CSValueStore extends CSContextController {

    private SharedPreferences preferences;

    public CSValueStore(String name) {
        preferences = getPreferences(name);
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    public void clear(String key) {
        Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public boolean has(String key) {
        return preferences.contains(key);
    }

    public <T extends CSJsonData> T load(T data, String key) {
        String loadString = loadString(key);
        if (no(loadString)) return null;
        Object dataMap = cs.android.json.CSJsonKt.fromJson(loadString);
        if (is(dataMap)) data.load((CSMap<String, Object>) dataMap);
        return data;
    }

    public boolean loadBoolean(String key) {
        return loadBoolean(key, false);
    }

    public boolean loadBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public int integer(String key) {
        return preferences.getInt(key, 0);
    }

    public int integer(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    public double loadDouble(String key) {
        return loadDouble(key, 0);
    }

    public double loadDouble(String key, double defaultValue) {
        return preferences.getFloat(key, (float) defaultValue);
    }

    public Long loadLong(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    public @Nullable String loadString(String key) {
        return preferences.getString(key, null);
    }

    public String loadString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public void put(String key, Boolean value) {
        if (no(value)) clear(key);
        else {
            Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }
    }

    public void put(String key, Integer value) {
        if (no(value)) clear(key);
        else {
            Editor editor = preferences.edit();
            editor.putInt(key, value);
            editor.apply();
        }
    }


    public void put(String key, Double value) {
        if (no(value)) clear(key);
        else {
            Editor editor = preferences.edit();
            editor.putFloat(key, value.floatValue());
            editor.apply();
        }
    }

    public void put(String key, Object data) {
        if (no(data)) clear(key);
        else put(key, toJson(data));
    }

    public <T extends CSJsonData> CSList<T> loadList(Class<T> type, String key) {
        return createList(type, (CSList<CSMap<String, Object>>) loadJson(key));
    }

    public void put(String key, Long value) {
        if (no(value)) clear(key);
        else {
            Editor editor = preferences.edit();
            editor.putLong(key, value);
            editor.apply();
        }
    }

    public String put(String key, String value) {
        if (no(value)) clear(key);
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
        return value;
    }

    private Object loadJson(String key) {
        String loadString = loadString(key);
        if (empty(loadString)) return null;
        return cs.android.json.CSJsonKt.fromJson(loadString);
    }

    protected SharedPreferences getPreferences(String key) {
        return context().getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    protected void put(String... keysValues) {
        Editor editor = preferences.edit();
        for (CSMapItem<String, String> keyValue : iterate(map(keysValues)))
            editor.putString(keyValue.key(), keyValue.value());
        editor.apply();
    }

}
