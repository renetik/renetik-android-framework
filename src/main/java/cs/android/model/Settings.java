package cs.android.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;

import cs.android.json.JSON;
import cs.android.json.JSONArray;
import cs.android.json.JSONData;
import cs.android.json.JSONObject;
import cs.android.json.JSONType;
import cs.android.viewbase.CSContextController;
import cs.java.collections.CSList;
import cs.java.collections.CSMapItem;

import static cs.java.lang.Lang.*;

public class Settings extends CSContextController {

    public static final String SETTING_USERNAME = "username";
    public static final String SETTING_PASSWORD = "password";
    public static Settings instance = new Settings();
    private SharedPreferences preferences;

    public Settings() {
        setDefault();
    }

    public Settings(String name) {
        setId(name);
    }

    public static Settings get() {
        return instance;
    }

    public void setId(String name) {
        preferences = getPreferences(name);
    }

    public void setDefault() {
        preferences = getPreferences("settings");
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    public void clear(String key) {
        Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public void clearCredentials() {
        clear(SETTING_USERNAME);
        clear(SETTING_PASSWORD);
    }

    public boolean has(String key) {
        return preferences.contains(key);
    }

    public <T extends JSONData> T load(T data, String key) {
        String loadString = loadString(key);
        if (no(loadString)) return null;
        JSONObject json = json(loadString).asObject();
        if (no(json)) return null;
        data.load(json);
        return data;
    }

    public JSONArray loadArray(String key) {
        JSONType container = loadJSONType(key);
        return is(container) ? container.asArray() : null;
    }

    public boolean loadBoolean(String key) {
        return loadBoolean(key, false);
    }

    public boolean loadBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public int loadInteger(String key) {
        return preferences.getInt(key, 0);
    }

    public int loadInteger(String key, int defaultValue) {
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

    public JSONObject loadObject(String key) {
        JSONType container = loadJSONType(key);
        return is(container) ? container.asObject() : null;
    }

    public String loadString(String key) {
        return preferences.getString(key, null);
    }

    public String loadString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public void save(String key, Boolean value) {
        if (no(value)) clear(key);
        else {
            Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }
    }

    public void save(String key, Integer value) {
        if (no(value)) clear(key);
        else {
            Editor editor = preferences.edit();
            editor.putInt(key, value);
            editor.apply();
        }
    }


    public void save(String key, Double value) {
        if (no(value)) clear(key);
        else {
            Editor editor = preferences.edit();
            editor.putFloat(key, value.floatValue());
            editor.apply();
        }
    }

    public void save(String key, JSONData data) {
        if (no(data)) clear(key);
        else save(key, data.data().toJSONString());
    }

    public <T extends JSONData> void save(String key, CSList<T> data) {
        if (no(data)) clear(key);
        else save(key, JSON.create(data).toJSONString());
    }

    public void save(String key, Long value) {
        if (no(value)) clear(key);
        else {
            Editor editor = preferences.edit();
            editor.putLong(key, value);
            editor.apply();
        }
    }

    public <T extends JSONData> void save(String key, Map<String, T> data) {
        if (no(data)) clear(key);
        else save(key, JSON.create(data).toJSONString());
    }

    public void save(String key, String value) {
        if (no(value)) clear(key);
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private JSONType loadJSONType(String key) {
        String loadString = loadString(key);
        if (empty(loadString)) return null;
        return json(loadString);
    }

    protected SharedPreferences getPreferences(String key) {
        return context().getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    protected void save(String... keysValues) {
        Editor editor = preferences.edit();
        for (CSMapItem<String, String> keyValue : iterate(map(keysValues)))
            editor.putString(keyValue.key(), keyValue.value());
        editor.apply();
    }

}
