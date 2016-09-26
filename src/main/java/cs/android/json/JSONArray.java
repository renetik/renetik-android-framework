package cs.android.json;

import org.json.JSONException;

import java.util.Iterator;
import java.util.List;

import cs.java.collections.CSIterator;

import static cs.java.lang.Lang.error;
import static cs.java.lang.Lang.exception;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.warn;

public class JSONArray extends JSONType implements Iterable<JSONType> {

    private final org.json.JSONArray _value;

    public JSONArray() {
        this(new org.json.JSONArray());
    }

    public JSONArray(org.json.JSONArray value) {
        super(value);
        _value = value;
    }

    public void add(boolean value) {
        add(JSON.create(value));
    }

    public void add(double value) {
        add(JSON.create(value));
    }

    public void add(String value) {
        add(JSON.create(value));
    }

    public JSONArray getArray(int index) {
        JSONType value = get(index);
        if (is(value)) {
            JSONArray typeValue = value.asArray();
            if (is(typeValue)) return typeValue;
            throw exception("Expected JSONArray, found ", value.getValue());
        }
        return null;
    }

    public Boolean getBoolean(int index) {
        JSONType value = get(index);
        if (is(value)) {
            JSONBoolean typeValue = value.asJSONBoolean();
            if (is(typeValue)) return typeValue.get();
            throw exception("Expected Boolean, found ", value.getValue());
        }
        return null;
    }

    public Number getNumber(int index) {
        JSONType value = get(index);
        if (is(value)) {
            JSONNumber typeValue = value.asJSONNumber();
            if (is(typeValue)) return typeValue.get();
            throw exception("Expected Number, found ", value.getValue());
        }
        return null;
    }

    public JSONObject getObject(int index) {
        JSONType value = get(index);
        if (is(value)) {
            JSONObject typeValue = value.asObject();
            if (is(typeValue)) return typeValue;
            throw exception("Expected JSONObject, found ", value.getValue());
        }
        return null;
    }

    public String getString(int index) {
        JSONType value = get(index);
        if (is(value)) {
            JSONString typeValue = value.asJSONString();
            if (is(typeValue)) return typeValue.get();
            throw exception("Expected String, found ", value.getValue());
        }
        return null;
    }

    public JSONArray add(List objects) {
        for (Object object : objects) add(JSON.create(object));
        return this;
    }

    public void add(JSONType value) {
        set(getSize(), value);
    }

    public JSONArray asArray() {
        return this;
    }

    public JSONType get(int index) {
        try {
            return JSON.create(_value.get(index));
        } catch (JSONException e) {
            warn(e);
        }
        return null;
    }

    public int getSize() {
        return _value.length();
    }

    public Iterator<JSONType> iterator() {
        return new CSIterator<JSONType>(getSize()) {
            public JSONType getValue() {
                return get(index());
            }
        };
    }

    public void set(int index, JSONType type) {
        try {
            _value.put(index, type.getValue());
        } catch (JSONException e) {
            warn(e);
        }
    }

}