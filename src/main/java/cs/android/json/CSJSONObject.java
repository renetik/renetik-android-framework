package cs.android.json;

import org.json.JSONException;

import java.util.Iterator;
import java.util.Map;

import cs.java.collections.CSMap;

import static cs.java.lang.CSLang.asBool;
import static cs.java.lang.CSLang.error;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.map;
import static cs.java.lang.CSLang.no;
import static cs.java.lang.CSLang.warn;

public class CSJSONObject extends CSJSONType implements Iterable<String> {

    private final org.json.JSONObject _value;

    public CSJSONObject() {
        this(new org.json.JSONObject());
    }

    public CSJSONObject(org.json.JSONObject value) {
        super(value);
        this._value = value;
    }

    public boolean contains(String key) {
        return is(get(key));
    }

    public CSJSONType get(String key) {
        Object valueKey = null;
        try {
            valueKey = _value.get(key);
        } catch (Exception e) {
        }
        if (is(valueKey)) return CSJSON.create(valueKey);
        return null;
    }

    public CSJSONArray getArray(String key) {
        CSJSONType value = get(key);
        if (is(value)) {
            CSJSONArray typeValue = value.asArray();
            if (is(typeValue)) return typeValue;
            warn("Expected JSONArray, found ", value.getJSONValue());
        }
        return null;
    }

    public Boolean getBoolean(String key) {
        CSJSONType value = get(key);
        if (is(value)) {
            return asBool(value.getJSONValue());
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
        CSJSONType value = get(key);
        if (is(value)) {
            CSJSONNumber typevalue = value.asJSONNumber();
            if (is(typevalue)) return typevalue.getValue();
            warn("Expected Number, found ", value.getJSONValue());
        }
        return null;
    }

    public CSJSONObject getObject(String key) {
        CSJSONType value = get(key);
        if (is(value)) {
            CSJSONObject typevalue = value.asObject();
            if (is(typevalue)) return typevalue;
            warn("Expected JSONObject, found ", value.getJSONValue(), " in ", asJSONString());
        }
        return null;
    }

    public String getString(String key) {
        CSJSONType value = get(key);
        if (is(value)) {
            CSJSONString typevalue = value.asJSONString();
            if (is(typevalue)) return typevalue.getValue();
            warn("Expected String, found ", value.getJSONValue());
        }
        return null;
    }

    public CSJSONObject asObject() {
        return this;
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        Boolean value = getBoolean(key);
        if (no(value)) return defaultValue;
        return value;
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

    public void put(String key, CSJSONType value) {
        try {
            this._value.put(key, value.getJSONValue());
        } catch (JSONException e) {
            error(e);
        }
    }

    public void remove(String key) {
        _value.remove(key);
    }

    public void put(String key, Boolean value) {
        put(key, CSJSON.create(value));
    }

    public void put(String key, Number value) {
        put(key, CSJSON.create(value));
    }

    public void put(String key, String value) {
        put(key, CSJSON.create(value));
    }

    public CSJSONObject put(Map data) {
        for (Object key : data.keySet()) put(key + "", CSJSON.create(data.get(key)));
        return this;
    }

    public CSMap<String, Object> getValue() {
        CSMap<String, Object> value = map();
        for (String key : this)
            value.put(key, get(key).getValue());
        return value;
    }
}
