package cs.android.json;

import org.json.JSONException;

import java.util.Iterator;
import java.util.Map;

import static cs.java.lang.Lang.asBool;
import static cs.java.lang.Lang.error;
import static cs.java.lang.Lang.exception;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.json;
import static cs.java.lang.Lang.map;
import static cs.java.lang.Lang.no;
import static cs.java.lang.Lang.warn;

public class JSONObject extends JSONType implements Iterable<String> {

    private final org.json.JSONObject _value;

    public JSONObject() {
        this(new org.json.JSONObject());
    }

    public JSONObject(org.json.JSONObject value) {
        super(value);
        this._value = value;
    }

    public <T> Map<String, T> asMap(Class<T> valueType) {
        Map<String, T> value = map();
        for (String key : this)
            value.put(key, (T) get(key).getValue());
        return value;
    }

    public boolean contains(String key) {
        return is(get(key));
    }

    public JSONType get(String key) {
        return getImpl(key);
    }

    public JSONArray getArray(String key) {
        JSONType value = get(key);
        if (is(value)) {
            JSONArray typeValue = value.asArray();
            if (is(typeValue)) return typeValue;
            warn("Expected JSONArray, found ", value.getValue());
        }
        return null;
    }

    public Boolean getBoolean(String key) {
        JSONType value = get(key);
        if (is(value)) {
            return asBool(value.getValue());
        }
        return null;
    }

    public Double getDouble(String key) {
        Number value = getNumber(key);
        if (is(value)) return value.doubleValue();
        return null;
    }

    public Integer getInteger(String key) {
        Number value = getNumber(key);
        if (is(value)) return value.intValue();
        return null;
    }

    public Long getLong(String key) {
        Number value = getNumber(key);
        if (is(value)) return value.longValue();
        return null;
    }

    public Number getNumber(String key) {
        JSONType value = get(key);
        if (is(value)) {
            JSONNumber typevalue = value.asJSONNumber();
            if (is(typevalue)) return typevalue.get();
            warn("Expected Number, found ", value.getValue());
        }
        return null;
    }

    public JSONObject getObject(String key) {
        JSONType value = get(key);
        if (is(value)) {
            JSONObject typevalue = value.asObject();
            if (is(typevalue)) return typevalue;
            warn("Expected JSONObject, found ", value.getValue(), " in ", asJSONString());
        }
        return null;
    }

    public String getString(String key) {
        JSONType value = get(key);
        if (is(value)) {
            JSONString typevalue = value.asJSONString();
            if (is(typevalue)) return typevalue.get();
            warn("Expected String, found ", value.getValue());
        }
        return null;
    }

    public JSONObject asObject() {
        return this;
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        Boolean value = getBoolean(key);
        if (no(value)) return defaultValue;
        return value;
    }

    public JSONType getImpl(String key) {
        Object valueKey = null;
        try {
            valueKey = _value.get(key);
        } catch (Exception e) {
        }
        if (is(valueKey)) return JSON.create(valueKey);
        return null;
    }

    public Integer getInteger(String key, Integer defaultValue) {
        Integer value = getInteger(key);
        if (no(value)) return defaultValue;
        return value;
    }

    public int getSize() {
        return _value.length();
    }

    public Iterator<String> iterator() {
        return _value.keys();
    }

    public void put(String key, JSONType value) {
        try {
            this._value.put(key, value.getValue());
        } catch (JSONException e) {
            error(e);
        }
    }

    public void remove(String key) {
        _value.remove(key);
    }

    public void put(String key, Boolean value) {
        put(key, JSON.create(value));
    }

    public void put(String key, Number value) {
        put(key, JSON.create(value));
    }

    public void put(String key, String value) {
        put(key, JSON.create(value));
    }

    public JSONObject put(Map data) {
        for (Object key : data.keySet()) put(key + "", JSON.create(data.get(key)));
        return this;
    }
}
